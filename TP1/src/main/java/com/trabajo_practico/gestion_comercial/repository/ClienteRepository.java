package com.trabajo_practico.gestion_comercial.repository;

import com.trabajo_practico.gestion_comercial.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
