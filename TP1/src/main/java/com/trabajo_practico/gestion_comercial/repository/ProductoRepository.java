package com.trabajo_practico.gestion_comercial.repository;


import com.trabajo_practico.gestion_comercial.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
