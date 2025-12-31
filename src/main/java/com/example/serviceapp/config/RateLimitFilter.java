package com.example.serviceapp.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate Limiting Filter để ngăn chặn spam và abuse trên form liên hệ.
 * Sử dụng Token Bucket algorithm để giới hạn số request từ mỗi IP.
 * 
 * <p>Cấu hình:</p>
 * <ul>
 *   <li>Rate limit: 3 requests trong 10 giây per IP</li>
 *   <li>Áp dụng cho: /lien-he/them-moi</li>
 *   <li>Response khi vượt limit: 429 Too Many Requests</li>
 * </ul>
 * 
 * @author GSTS Team
 * @version 1.0
 */
@Component
@Order(-50)  // Chạy sau NoSessionFilter (-100) nhưng trước Security Filter
public class RateLimitFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(RateLimitFilter.class);
    
    // Giới hạn: 3 requests trong 10 giây
    private static final int MAX_REQUESTS = 3;
    private static final long TIME_WINDOW_MS = 10_000; // 10 giây
    
    // Cache request history cho mỗi IP
    private final ConcurrentHashMap<String, RequestHistory> requestHistories = new ConcurrentHashMap<>();
    
    /**
     * Class lưu trữ lịch sử request của một IP.
     */
    private static class RequestHistory {
        private long lastRequestTime;
        private int requestCount;
        
        public RequestHistory() {
            this.lastRequestTime = System.currentTimeMillis();
            this.requestCount = 1;
        }
        
        public synchronized boolean tryAcquire() {
            long now = System.currentTimeMillis();
            long timeSinceLastRequest = now - lastRequestTime;
            
            // Reset counter nếu quá time window
            if (timeSinceLastRequest > TIME_WINDOW_MS) {
                lastRequestTime = now;
                requestCount = 1;
                return true;
            }
            
            // Kiểm tra có vượt limit không
            if (requestCount < MAX_REQUESTS) {
                requestCount++;
                return true;
            }
            
            return false;
        }
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String path = request.getRequestURI();
        
        // Chỉ áp dụng rate limit cho form liên hệ
        if (shouldApplyRateLimit(path)) {
            String ip = getClientIP(request);
            
            // Lấy hoặc tạo RequestHistory cho IP này
            RequestHistory history = requestHistories.computeIfAbsent(ip, k -> new RequestHistory());
            
            // Kiểm tra rate limit
            if (!history.tryAcquire()) {
                logger.warn("⚠️ Rate limit exceeded for IP: {} on path: {}", ip, path);
                handleRateLimitExceeded(response);
                return;
            }
            
            logger.debug("✅ Rate limit OK for IP: {} on path: {}", ip, path);
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * Kiểm tra xem path có cần áp dụng rate limit không.
     * 
     * @param path Request URI path
     * @return true nếu cần rate limit
     */
    private boolean shouldApplyRateLimit(String path) {
        return path.startsWith("/lien-he/them-moi");
    }
    
    /**
     * Xử lý khi vượt rate limit.
     * 
     * @param response HTTP response
     * @throws IOException if writing response fails
     */
    private void handleRateLimitExceeded(HttpServletResponse response) throws IOException {
        response.setStatus(429); // 429 Too Many Requests
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String jsonResponse = """
            {
                "status": "error",
                "message": "Quá nhiều request. Vui lòng thử lại sau vài giây.",
                "code": 429
            }
            """;
        
        response.getWriter().write(jsonResponse);
    }
    
    /**
     * Lấy địa chỉ IP thực của client, xử lý cả trường hợp có reverse proxy.
     * 
     * @param request HTTP request
     * @return Client IP address
     */
    private String getClientIP(HttpServletRequest request) {
        // Kiểm tra X-Forwarded-For header (khi có reverse proxy)
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isEmpty()) {
            // X-Forwarded-For có thể chứa nhiều IP, lấy IP đầu tiên
            return xfHeader.split(",")[0].trim();
        }
        
        // Kiểm tra X-Real-IP header (nginx)
        String realIP = request.getHeader("X-Real-IP");
        if (realIP != null && !realIP.isEmpty()) {
            return realIP.trim();
        }
        
        // Fallback: lấy IP trực tiếp
        return request.getRemoteAddr();
    }
}

