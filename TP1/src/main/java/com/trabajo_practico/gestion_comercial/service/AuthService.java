package com.trabajo_practico.gestion_comercial.service;

import com.trabajo_practico.gestion_comercial.model.Usuario;
import com.trabajo_practico.gestion_comercial.repository.UsuarioRepository;
import com.trabajo_practico.gestion_comercial.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(String username, String password) {
        // Buscar el usuario de manera segura con Optional
        var usuarioOpt = usuarioRepository.findByUsername(username);

        // Si no se encuentra el usuario, lanzar una excepción
        var usuario = usuarioOpt.orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // Verificar la contraseña
        if (passwordEncoder.matches(password, usuario.getPassword())) {
            return jwtUtil.generateToken(username);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}
