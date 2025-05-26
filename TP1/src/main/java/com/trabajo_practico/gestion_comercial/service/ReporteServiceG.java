package com.trabajo_practico.gestion_comercial.service;

import com.trabajo_practico.gestion_comercial.dto.MovimientoMensualDTO;

public interface ReporteServiceG {
    MovimientoMensualDTO obtenerMovimientoMensual(Integer mes, Integer anio);
}
