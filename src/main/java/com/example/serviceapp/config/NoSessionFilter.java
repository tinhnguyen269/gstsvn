package com.example.serviceapp.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(-100)  // Chạy trước Security Filter Chain
public class NoSessionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String path = request.getRequestURI();
        
        // Tắt session cho các trang customer (public) - không tạo session mới
        if (isPublicPath(path)) {
            // Wrap request để ngăn tạo session
            HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(request) {
                @Override
                public HttpSession getSession(boolean create) {
                    // Không bao giờ tạo session mới cho public pages
                    return create ? null : super.getSession(false);
                }
                
                @Override
                public HttpSession getSession() {
                    // Không tạo session mới
                    return null;
                }
            };
            
            // Wrap response để ngăn set cookie JSESSIONID
            HttpServletResponse wrappedResponse = new jakarta.servlet.http.HttpServletResponseWrapper(response) {
                @Override
                public void addCookie(jakarta.servlet.http.Cookie cookie) {
                    // Không thêm cookie JSESSIONID cho public pages
                    if (!cookie.getName().equals("JSESSIONID")) {
                        super.addCookie(cookie);
                    }
                }
            };
            
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } else {
            filterChain.doFilter(request, response);
        }
    }
    
    private boolean isPublicPath(String path) {
        return path.startsWith("/") && 
               !path.startsWith("/admin") && 
               !path.startsWith("/login") && 
               !path.startsWith("/logout") &&
               !path.startsWith("/user") &&
               !path.startsWith("/customer/css") &&
               !path.startsWith("/customer/js") &&
               !path.startsWith("/customer/images") &&
               !path.startsWith("/customer/fonts");
    }
}

