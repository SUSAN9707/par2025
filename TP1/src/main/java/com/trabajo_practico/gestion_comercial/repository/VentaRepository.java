package com.trabajo_practico.gestion_comercial.repository;

import com.trabajo_practico.gestion_comercial.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    @Query("SELECT v FROM Venta v WHERE MONTH(v.fecha) = :mes AND YEAR(v.fecha) = :anio")
    List<Venta> findByMesYAnio(@Param("mes") int mes, @Param("anio") int anio);

    List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

}
