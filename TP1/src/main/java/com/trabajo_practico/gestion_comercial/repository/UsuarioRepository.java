package com.trabajo_practico.gestion_comercial.repository;

import com.trabajo_practico.gestion_comercial.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
