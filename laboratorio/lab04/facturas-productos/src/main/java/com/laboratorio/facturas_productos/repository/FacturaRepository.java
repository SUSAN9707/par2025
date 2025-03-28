package com.laboratorio.facturas_productos.main.repository;

import com.laboratorio.facturas_productos.main.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
}