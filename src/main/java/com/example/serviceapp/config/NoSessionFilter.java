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
 * Giúp giảm tải cho server bằng cách không tạo JSESSIONID cho các request public.
 *
 * @author GSTS Team
 * @version 1.0
 */
@Component
@Order(-100)  // Chạy trước Security Filter Chain
public class NoSessionFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(NoSessionFilter.class);
    private static final String JSESSIONID = "JSESSIONID";

    // Các prefix paths cho trang public (customer)
    private static final Set<String> PUBLIC_PATH_PREFIXES = Set.of(
            "/dich-vu",
            "/tin-tuc",
            "/du-an",
            "/gioi-thieu",
            "/lien-he",
            "/bao-gia",
            "/api/contact",
            "/sitemap.xml"
    );

    // Các prefix paths cần được loại trừ (admin, auth)
    private static final Set<String> EXCLUDED_PATH_PREFIXES = Set.of(
            "/admin",
            "/login",
            "/logout",
            "/register",
            "/user",
            "/forgot-password",
            "/reset-password"
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
     * @param path Request URI path
     * @return true nếu là public path, false nếu không
     */
    private boolean isPublicPath(String path) {
        // Root path "/" là public
        if ("/".equals(path)) {
            return true;
        }

        // Loại trừ các paths admin/auth trước
        for (String excludedPrefix : EXCLUDED_PATH_PREFIXES) {
            if (path.contains(excludedPrefix)) {
                return false;
            }
        }

        // Kiểm tra các public path prefixes
        for (String publicPrefix : PUBLIC_PATH_PREFIXES) {
            if (path.startsWith(publicPrefix)) {
                return true;
            }
        }

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

