package com.trabajo_practico.gestion_comercial.service;

import com.trabajo_practico.gestion_comercial.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ReporteServiceG {
    MovimientoMensualDTO obtenerMovimientoMensual(Integer mes, Integer anio);

    List<MovimientoReporteDTO> obtenerComprasPorRango(LocalDateTime inicio, LocalDateTime fin);

    List<MovimientoReporteDTO> obtenerVentasPorRango(LocalDateTime inicio, LocalDateTime fin);

    List<InventarioProductoDTO> obtenerInventarioActual();

    InventarioCompletoDTO obtenerInventarioProductos();

    List<ProductoMasVendidoDTO> obtenerProductosMasVendidos(LocalDateTime inicio, LocalDateTime fin);

    List<TopEntidadDTO> obtenerTopClientes(LocalDateTime inicio, LocalDateTime fin);

    List<TopEntidadDTO> obtenerTopProveedores(LocalDateTime inicio, LocalDateTime fin);

    UtilidadDTO obtenerReporteUtilidad(LocalDateTime inicio, LocalDateTime fin);
}
