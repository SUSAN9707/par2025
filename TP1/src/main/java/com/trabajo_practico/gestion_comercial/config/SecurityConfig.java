package com.trabajo_practico.gestion_comercial.config;

import com.trabajo_practico.gestion_comercial.filters.JwtAuthenticationFilter;
import com.trabajo_practico.gestion_comercial.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final JwtUtil jwtUtil;

    @Value("${api.version}") // Inyecta el valor de api.version desde application.properties
    private String apiVersion;
    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())  // Usa el CorsConfigurationSource definido arriba
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/" + apiVersion + "/usuarios/login",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/**"
                        ).permitAll()
                        .requestMatchers("/" + apiVersion + "/**").authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
