package com.example.serviceapp.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

/**
 * Filter để ngăn tạo session cho các trang public (customer pages).
 * 
 * <p><b>Mục đích:</b></p>
 * <ul>
 *   <li>✅ Trang Public: KHÔNG tạo session, KHÔNG set JSESSIONID cookie</li>
 *   <li>✅ Trang Admin: TẠO session khi login, SET JSESSIONID cookie</li>
 * </ul>
 * 
 * <p><b>Lợi ích:</b></p>
 * <ul>
 *   <li>Giảm tải server (không tạo session cho mỗi visitor)</li>
 *   <li>Tăng performance (không cần lưu session data cho public pages)</li>
 *   <li>Tăng bảo mật (giảm surface attack cho session fixation)</li>
 *   <li>SEO-friendly (không set cookie cho bots/crawlers)</li>
 * </ul>
 * 
 * <p><b>Order:</b> -100 (chạy TRƯỚC Security Filter Chain)</p>
 * 
 * @author GSTS Team
 * @version 2.0
 */
@Component
@Order(-100)  // Chạy trước Security Filter Chain (-100 < default Security Filter 0)
public class NoSessionFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(NoSessionFilter.class);
    private static final String JSESSIONID = "JSESSIONID";

    // ==================== PUBLIC PATHS (KHÔNG TẠO SESSION) ====================
    
    /**
     * Các paths cho trang public/customer (KHÔNG tạo session, KHÔNG set JSESSIONID).
     * Bao gồm: homepage, dịch vụ, tin tức, dự án, giới thiệu, liên hệ, báo giá, API, sitemap.
     */
    private static final Set<String> PUBLIC_PATH_PREFIXES = Set.of(
            "/dich-vu",      // Trang danh sách & chi tiết dịch vụ
            "/tin-tuc",      // Trang blog/tin tức
            "/du-an",        // Trang dự án
            "/gioi-thieu",   // Trang giới thiệu công ty
            "/lien-he",      // Trang liên hệ + form submit
            "/bao-gia",      // Trang báo giá
            "/api/contact",  // API liên hệ (nếu có)
            "/sitemap.xml"   // SEO sitemap
    );

    // ==================== ADMIN PATHS (CẦN TẠO SESSION) ====================
    
    /**
     * Các paths cho admin/auth pages (CẦN tạo session, SET JSESSIONID).
     * Bao gồm: admin panel, login, logout, user management.
     */
    private static final Set<String> EXCLUDED_PATH_PREFIXES = Set.of(
            "/admin",            // Admin panel (cần authentication)
            "/login",            // Login page (cần session để login)
            "/logout",           // Logout (cần session để invalidate)
            "/register",         // Register page (nếu có)
            "/user",             // User management
            "/forgot-password",  // Quên mật khẩu
            "/reset-password"    // Reset mật khẩu
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (isPublicPath(path)) {
            logger.debug("Blocking session creation for public path: {}", path);
            filterChain.doFilter(wrapRequest(request), wrapResponse(response));
        } else {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * Kiểm tra xem path có phải là public path không (cần chặn session).
     * 
     * <p><b>Logic kiểm tra:</b></p>
     * <ol>
     *   <li>Nếu là "/" (homepage) → public</li>
     *   <li>Nếu chứa admin/login/user paths → KHÔNG phải public (cần session)</li>
     *   <li>Nếu bắt đầu với /dich-vu, /tin-tuc, etc. → public</li>
     *   <li>Ngược lại → KHÔNG phải public (an toàn hơn)</li>
     * </ol>
     *
     * @param path Request URI path (e.g., "/", "/dich-vu/web-development", "/admin/dashboard")
     * @return true nếu là public path (KHÔNG tạo session), false nếu cần session
     */
    private boolean isPublicPath(String path) {
        // Step 1: Root path "/" và "/home" là public
        if ("/".equals(path) || "/home".equals(path)) {
            return true;
        }

        // Step 2: Loại trừ các paths admin/auth TRƯỚC (priority cao nhất)
        // VD: /admin/dashboard, /login, /user/list → false (cần session)
        for (String excludedPrefix : EXCLUDED_PATH_PREFIXES) {
            if (path.contains(excludedPrefix)) {
                return false; // ❌ KHÔNG phải public → CẦN session
            }
        }

        // Step 3: Kiểm tra các public path prefixes
        // VD: /dich-vu/web-dev, /tin-tuc/post-1, /lien-he → true (public)
        for (String publicPrefix : PUBLIC_PATH_PREFIXES) {
            if (path.startsWith(publicPrefix)) {
                return true; // ✅ Public path → KHÔNG tạo session
            }
        }

        // Step 4: Default - KHÔNG phải public (an toàn hơn)
        // Các paths không match ở trên sẽ ĐƯỢC TẠO session
        return false;
    }

    /**
     * Wrap HttpServletRequest để ngăn tạo session mới.
     *
     * @param request Original request
     * @return Wrapped request không tạo session
     */
    private HttpServletRequest wrapRequest(HttpServletRequest request) {
        return new HttpServletRequestWrapper(request) {
            @Override
            public HttpSession getSession(boolean create) {
                // Không tạo session mới cho public pages
                return create ? null : super.getSession(false);
            }

            @Override
            public HttpSession getSession() {
                // Luôn trả về null (không tạo session)
                return null;
            }
        };
    }

    /**
     * Wrap HttpServletResponse để ngăn set JSESSIONID cookie.
     *
     * @param response Original response
     * @return Wrapped response không set JSESSIONID
     */
    private HttpServletResponse wrapResponse(HttpServletResponse response) {
        return new HttpServletResponseWrapper(response) {
            @Override
            public void addCookie(Cookie cookie) {
                // Chỉ thêm cookie nếu không phải JSESSIONID
                if (!JSESSIONID.equals(cookie.getName())) {
                    super.addCookie(cookie);
                } else {
                    logger.debug("Blocked JSESSIONID cookie for public page");
                }
            }
        };
    }
}

