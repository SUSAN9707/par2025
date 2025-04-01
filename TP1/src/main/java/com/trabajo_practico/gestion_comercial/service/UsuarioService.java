package com.trabajo_practico.gestion_comercial.service;

import com.trabajo_practico.gestion_comercial.model.Usuario;
import com.trabajo_practico.gestion_comercial.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para encontrar un usuario por su username
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // Método para guardar un nuevo usuario
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
