package com.tinqinacademy.authentication.rest.interceptors;

import com.tinqinacademy.authentication.api.apimapping.AdminMapping;
import com.tinqinacademy.authentication.core.security.JwtUtil;
import com.tinqinacademy.authentication.persistence.entities.User;
import com.tinqinacademy.authentication.persistence.models.RoleType;
import com.tinqinacademy.authentication.persistence.repositories.UsersRepository;
import com.tinqinacademy.authentication.rest.context.LoggedUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserSecurityInterceptor implements HandlerInterceptor {
    private final LoggedUser loggedUser;
    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (null == authorization || !authorization.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        String jwtToken = authorization.substring(7);
        if (!jwtUtil.isTokenValid(jwtToken)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        String userId = jwtUtil.extractUserId(jwtToken);
        Optional<User> userOptional = usersRepository.findById(UUID.fromString(userId));
        if (userOptional.isEmpty()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        if (isAdminPath(request.getRequestURI()) && !userOptional.get().getRoleType().equals(RoleType.ADMIN)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        loggedUser.setLoggedUser(userOptional.get());
        loggedUser.setToken(jwtToken);
        return true;
    }


    private boolean isAdminPath(String requestUri) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return AdminMapping.getPaths().stream()
                .anyMatch(path -> antPathMatcher.match(path, requestUri));
    }
}