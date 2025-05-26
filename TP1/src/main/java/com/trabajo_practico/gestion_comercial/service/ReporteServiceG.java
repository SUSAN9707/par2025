package com.trabajo_practico.gestion_comercial.service;

import com.trabajo_practico.gestion_comercial.dto.InventarioCompletoDTO;
import com.trabajo_practico.gestion_comercial.dto.InventarioProductoDTO;
import com.trabajo_practico.gestion_comercial.dto.MovimientoMensualDTO;

import java.util.List;

public interface ReporteServiceG {
    MovimientoMensualDTO obtenerMovimientoMensual(Integer mes, Integer anio);
    List<InventarioProductoDTO> obtenerInventarioActual();

    InventarioCompletoDTO obtenerInventarioProductos();  // Esta es la firma correcta

}
