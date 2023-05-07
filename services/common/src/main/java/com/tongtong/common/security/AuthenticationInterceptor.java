package com.tongtong.common.security;

import com.tongtong.common.entity.UserAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    //    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return false;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            logger.info("No Authorization header present in the request; requestContext={}", request.toString());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().println("User does not have an authentication token");
            return false;
        }

        logger.debug("Auth header : " + authHeader);

        UserAuth userAuth = getUserCred(authHeader);
        if (userAuth == null) {
            logger.info("Null access token");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().println("User does not have a valid authentication token");
            return false;
        }

        AppAuthentication appAuthentication = new AppAuthentication(userAuth,
                request.isSecure(), authHeader);
        appAuthentication.setAuthenticated(true);
        AppSecurityContext appSecurityContext = new AppSecurityContext(appAuthentication);
        request.setAttribute("AppSecurityContext", appSecurityContext);
        SecurityContextHolder.setContext(appSecurityContext);
        logger.debug("User authenticated; context={}", userAuth);

        return true;
    }

    private UserAuth getUserCred(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            logger.error("Authorization not provided; authHeader={}", authHeader);
            return null;
        }

        String authToken = authHeader.substring("Bearer".length()).trim();

        JwtUtility jwtUtility = new JJwtUtility();
        UserAuth userAuth = jwtUtility.parseToken(authToken);

        return userAuth;
    }

}

