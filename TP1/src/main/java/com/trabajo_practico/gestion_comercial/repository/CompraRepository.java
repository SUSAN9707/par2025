package com.trabajo_practico.gestion_comercial.repository;

import com.trabajo_practico.gestion_comercial.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    @Query("SELECT c FROM Compra c WHERE MONTH(c.fecha) = :mes AND YEAR(c.fecha) = :anio")
    List<Compra> findByMesYAnio(@Param("mes") int mes, @Param("anio") int anio);
}
