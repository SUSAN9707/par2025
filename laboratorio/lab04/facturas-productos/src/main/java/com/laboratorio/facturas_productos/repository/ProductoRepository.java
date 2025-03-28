package com.laboratorio.facturas_productos.repository;


import com.laboratorio.facturas_productos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
