package com.tongtong.common.security;

import com.tongtong.common.entity.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationInterceptor implements HandlerInterceptor {


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // Get the resource method which matches with the requested URL
        // Extract the roles declared by it
        Method resourceMethod = ((HandlerMethod) handler).getMethod();

        List<Role> allowedRoles = extractRoles(resourceMethod);

        if (allowedRoles.isEmpty()) {
            // Get the resource class which matches with the requested URL
            // Extract the roles declared by it
            Class<?> resourceClass = resourceMethod.getDeclaringClass();
            allowedRoles = extractRoles(resourceClass);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!allowedRoles.isEmpty() && !checkPermissions(allowedRoles)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("User not authorized");
            return false;
        }

        return true;
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

    private boolean checkPermissions(List<Role> allowedRoles) {
        // Check if the user contains one of the allowed roles
        // Throw an Exception if the user has not permission to execute the method
        AppAuthentication authentication = (AppAuthentication) SecurityContextHolder.getContext().getAuthentication();
        for (Role allowedRole : allowedRoles) {
            if (authentication.getAuthorities().contains(Role.getRole(allowedRole.toString()))) {
                return true;
            }
        }
        return false;
    }

}