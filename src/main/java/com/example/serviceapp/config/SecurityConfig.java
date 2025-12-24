package com.example.serviceapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.ForwardedHeaderFilter;

/**
 * Cấu hình Spring Security cho ứng dụng GSTS.
 * 
 * <p>Bao gồm:</p>
 * <ul>
 *   <li>Authorization rules (phân quyền ADMIN, EMPLOYEE)</li>
 *   <li>Form-based authentication (login/logout)</li>
 *   <li>Session management</li>
 *   <li>CSRF protection (disable cho public API)</li>
 *   <li>Reverse proxy support (X-Forwarded headers)</li>
 * </ul>
 * 
 * @author GSTS Team
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ==================== CONSTANTS ====================
    
    // Static resources patterns
    private static final String[] STATIC_RESOURCES = {
            "/admin/css/**", "/admin/js/**", "/admin/assets/**",
            "/customer/css/**", "/customer/js/**", "/customer/images/**", "/customer/fonts/**",
            "/webjars/**", "/favicon.ico"
    };

    // Public pages patterns (no CSRF protection)
    private static final String[] PUBLIC_PAGES = {
            "/", "/home",
            "/dich-vu/**", "/tin-tuc/**", "/du-an/**",
            "/gioi-thieu", "/lien-he/**", "/bao-gia/**",
            "/customer/**", "/api/**", "/sitemap.xml",
    };

    // Authentication pages
    private static final String LOGIN_PAGE = "/login";
    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String DEFAULT_SUCCESS_URL = "/admin/service/list";
    private static final String LOGIN_FAILURE_URL = "/login?error=true";
    private static final String LOGOUT_URL = "/logout";
    private static final String LOGOUT_SUCCESS_URL = "/login?logout";

    // ==================== DEPENDENCIES ====================
    
    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // ==================== BEANS ====================
    
    /**
     * Filter hỗ trợ X-Forwarded headers từ reverse proxy (nginx, apache).
     * Cần thiết khi deploy lên VPS với proxy.
     * 
     * @return ForwardedHeaderFilter instance
     */
    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }

    /**
     * Cấu hình Security Filter Chain chính.
     * 
     * @param http HttpSecurity object
     * @return SecurityFilterChain configured
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ========== Authorization Rules ==========
                .authorizeHttpRequests(auth -> auth
                        // Static resources - public access
                        .requestMatchers(STATIC_RESOURCES).permitAll()

                        // Admin-only endpoints
                        .requestMatchers("/user/**").hasRole("ADMIN")

                        // Admin & Employee endpoints
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "EMPLOYEE")

                        // All other requests - public access
                        .anyRequest().permitAll()
                )

                // ========== Form Login ==========
                .formLogin(form -> form
                        .loginPage(LOGIN_PAGE)
                        .loginProcessingUrl(LOGIN_PROCESSING_URL)
                        .defaultSuccessUrl(DEFAULT_SUCCESS_URL, true)
                        .failureUrl(LOGIN_FAILURE_URL)
                        .permitAll()
                )

                // ========== Logout ==========
                .logout(logout -> logout
                        .logoutUrl(LOGOUT_URL)
                        .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                // ========== Session Management ==========
                .sessionManagement(session -> session
                        // Chỉ tạo session khi cần (login/admin)
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        // Ngăn session fixation attacks
                        .sessionFixation().migrateSession()
                        // Max 1 session per user (optional - comment out if not needed)
                        // .maximumSessions(1)
                        // .maxSessionsPreventsLogin(false)
                )

                // ========== CSRF Protection ==========
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                PUBLIC_PAGES
                        )
                );



        return http.build();
    }

    /**
     * Authentication Manager bean.
     * 
     * @param http HttpSecurity object
     * @return AuthenticationManager configured
     * @throws Exception if configuration fails
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    /**
     * Password encoder bean (Delegating).
     * <p>
     * Sử dụng DelegatingPasswordEncoder để hỗ trợ nhiều format:
     * <ul>
     *   <li>{bcrypt} - BCrypt (default, recommended)</li>
     *   <li>{noop} - Plain text (chỉ dùng test)</li>
     *   <li>{pbkdf2} - PBKDF2</li>
     *   <li>{scrypt} - SCrypt</li>
     *   <li>{sha256} - SHA-256</li>
     * </ul>
     * <p>
     * Format trong database: {id}encodedPassword
     * VD: {bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
     * 
     * @return DelegatingPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

