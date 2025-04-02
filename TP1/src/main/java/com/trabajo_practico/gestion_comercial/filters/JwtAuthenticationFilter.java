package com.trabajo_practico.gestion_comercial.filters;

import com.trabajo_practico.gestion_comercial.security.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtTokenProvider;

    public JwtAuthenticationFilter(JwtUtil jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);

        if (token != null) {
            try {
                String username = jwtTokenProvider.extractUsername(token);

                if (jwtTokenProvider.validateToken(token, username)) {
                    var authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expirado");
                response.getWriter().flush();
                return;
            } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido");
                response.getWriter().flush();
                return;
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Error en la autenticación");
                response.getWriter().flush();
                return;
            }
        }
        String requestURI = request.getRequestURI();

        // Permitir acceso sin autenticación a Swagger y OpenAPI
        if (requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/v3/api-docs")|| requestURI.startsWith("/v2/usuarios/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"No estás autenticado. Debes proporcionar un token.\"}");
            response.setContentType("application/json");
            response.getWriter().flush();
            return;
        }

        if (!jwtTokenProvider.validateToken(token, jwtTokenProvider.extractUsername(token))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Token inválido o expirado.\"}");
            response.setContentType("application/json");
            response.getWriter().flush();
            return;
        }

        var authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
