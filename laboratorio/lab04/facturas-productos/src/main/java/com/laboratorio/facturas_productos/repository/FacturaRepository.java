package com.trabajo_practico.gestion_comercial.repository;

import com.trabajo_practico.gestion_comercial.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
}