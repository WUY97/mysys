package com.tongtong.common.security;

import com.tongtong.common.config.AppConfig;
import com.tongtong.common.entity.UserAuth;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {AppConfig.AUTHORIZATION_RESOURCE_PATH + "/*"})
public class AuthenticationFilter implements Filter {

    private static final int STATUS_OK = HttpServletResponse.SC_OK;
    private static final int STATUS_FORBIDDEN = HttpServletResponse.SC_FORBIDDEN;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化方法
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (isCorsPreflightRequest(request)) {
            response.setStatus(STATUS_OK);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            response.setStatus(STATUS_FORBIDDEN);
            response.getWriter().println("User does not have an authentication token");
            return;
        }

        UserAuth userAuth = getUserAuth(authHeader);
        if (userAuth == null) {
            response.setStatus(STATUS_FORBIDDEN);
            response.getWriter().println("User does not have a valid authentication token");
            return;
        }

        AppAuthentication appAuthentication = new AppAuthentication(userAuth,
                request.isSecure(), authHeader);
        appAuthentication.setAuthenticated(true);
        AppSecurityContext appSecurityContext = new AppSecurityContext(appAuthentication);
        request.setAttribute("AppSecurityContext", appSecurityContext);
        SecurityContextHolder.setContext(appSecurityContext);

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 销毁方法
    }

    private boolean isCorsPreflightRequest(HttpServletRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    private UserAuth getUserAuth(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return null;
        }

        String authToken = authHeader.substring("Bearer".length()).trim();

        JwtUtility jwtUtility = new JJwtUtility();

        return jwtUtility.parseToken(authToken);
    }
}