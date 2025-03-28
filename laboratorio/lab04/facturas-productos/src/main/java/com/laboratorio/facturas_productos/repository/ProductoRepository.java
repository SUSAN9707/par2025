package com.laboratorio.facturas_productos.main.repository;


import com.laboratorio.facturas_productos.main.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
