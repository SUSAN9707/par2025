package com.laboratorio.facturas_productos.repository;

import com.laboratorio.facturas_productos.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
}