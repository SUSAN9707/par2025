package com.trabajo_practico.gestion_comercial.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    // Generar una clave secreta segura de 256 bits para HS256
    private Key secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode("c2VjdXJlc2VjcmV0a2V5Zm9yamdzbmV4YW1wbGphdmFhcHBsaWNhdGlvbg=="));

    // Generar el token JWT
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora de validez
                .signWith(secretKey) // Usar la clave generada aquí
                .compact();
    }

    // Validar el token
    public boolean validateToken(String token, String username) {
        String usernameFromToken = extractUsername(token);
        return (usernameFromToken.equals(username) && !isTokenExpired(token));
    }

    // Extraer el username del token
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey) // Usa directamente `secretKey`
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Verificar si el token ha expirado
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey) // Usa directamente `secretKey`
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    // Obtener la autenticación del token
    public Authentication getAuthentication(String token) {
        String username = extractUsername(token);

        // Agregar una autoridad por defecto, como "ROLE_USER"
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
