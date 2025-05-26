package com.trabajo_practico.gestion_comercial.controller;

import com.trabajo_practico.gestion_comercial.dto.*;
import com.trabajo_practico.gestion_comercial.service.ReporteServiceG;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${api.version}/reportes")
public class ReporteControllerG {

    private final ReporteServiceG reporteService;

    public ReporteControllerG(ReporteServiceG reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/movimientos")
    public ResponseEntity<ApiResponse> getMovimientos(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer anio
    ) {
        MovimientoMensualDTO data = reporteService.obtenerMovimientoMensual(mes, anio);
        return ResponseEntity.ok(new ApiResponse("Reporte de movimientos cargado", HttpStatus.OK.value(), data));
    }

    @GetMapping("/inventario")
    public ResponseEntity<ApiResponse> obtenerReporteInventario() {
        InventarioCompletoDTO reporte = reporteService.obtenerInventarioProductos();
        return ResponseEntity.ok(new ApiResponse("Reporte de inventario generado", HttpStatus.OK.value(), reporte));
    }
}
