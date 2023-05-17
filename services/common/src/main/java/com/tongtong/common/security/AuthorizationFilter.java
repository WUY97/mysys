package com.tongtong.common.security;

import com.tongtong.common.config.AppConfig;
import com.tongtong.common.entity.Role;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {AppConfig.AUTHORIZATION_RESOURCE_PATH + "/*"})
public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Get the resource method which matches with the requested URL
        // Extract the roles declared by it
        HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler");
        Method resourceMethod = handlerMethod.getMethod();

        List<Role> allowedRoles = extractRoles(resourceMethod);

        if (allowedRoles.isEmpty()) {
            // Get the resource class which matches with the requested URL
            // Extract the roles declared by it
            Class<?> resourceClass = resourceMethod.getDeclaringClass();
            allowedRoles = extractRoles(resourceClass);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!allowedRoles.isEmpty() && !checkPermissions(allowedRoles, authentication)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("User not authorized");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    // Extract the roles from the annotated element
    private List<Role> extractRoles(AnnotatedElement annotatedElement) {
        List<Role> roles = new ArrayList<>();
        if (annotatedElement != null) {
            Secured secured = annotatedElement.getAnnotation(Secured.class);
            if (secured != null) {
                String[] allowedRoles = secured.value();
                for (String role : allowedRoles) {
                    roles.add(Role.getRole(role));
                }
            }
        }
        return roles;
    }

    private boolean checkPermissions(List<Role> allowedRoles, Authentication authentication) {
        // Check if the user contains one of the allowed roles
        // Throw an Exception if the user has no permission to execute the method
        AppAuthentication appAuthentication = (AppAuthentication) authentication;
        for (Role allowedRole : allowedRoles) {
            if (appAuthentication.getAuthorities().contains(Role.getRole(allowedRole.toString()))) {
                return true;
            }
        }
        return false;
    }
}