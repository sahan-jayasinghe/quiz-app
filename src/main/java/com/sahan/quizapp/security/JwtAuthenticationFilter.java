package com.sahan.quizapp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = extractTokenFromRequest(request);
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized");
                return;
            }
            String username = jwtTokenProvider.getUsernameFromToken(token);
            String roleStr = jwtTokenProvider.getRoleFromToken(token);
            request.setAttribute("username", username);
            request.setAttribute("role", roleStr);

            // Set Spring Security Authentication so method security and role checks work
            if (username != null && roleStr != null) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleStr);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(authority)
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            logger.error("JWT validation failed", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
