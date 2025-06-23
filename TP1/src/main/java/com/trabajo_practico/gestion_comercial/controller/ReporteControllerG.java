package com.trabajo_practico.gestion_comercial.controller;

import com.trabajo_practico.gestion_comercial.dto.*;
import com.trabajo_practico.gestion_comercial.service.ReporteServiceG;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

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

    @GetMapping("/compras")
    public  ResponseEntity<ApiResponse<List<MovimientoReporteDTO>>> obtenerComprasPorRango(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<MovimientoReporteDTO> compras = reporteService.obtenerComprasPorRango(inicio, fin);
        return ResponseEntity.ok(new ApiResponse("Reporte de compras generado", HttpStatus.OK.value(), compras));
    }
    @GetMapping("/ventas")
    public  ResponseEntity<ApiResponse<List<MovimientoReporteDTO>>> obtenerVentasPorRango(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<MovimientoReporteDTO> ventas = reporteService.obtenerVentasPorRango(inicio, fin);
        return ResponseEntity.ok(new ApiResponse("Reporte de ventas generado", HttpStatus.OK.value(),ventas));
    }
    @GetMapping("/productos-mas-vendidos")
    public ResponseEntity<ApiResponse<List<ProductoMasVendidoDTO>>> getProductosMasVendidos(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime fin
    ) {
        LocalDateTime inicioNuevo = inicio.toLocalDateTime();
        LocalDateTime finNuevo = fin.toLocalDateTime();
        List<ProductoMasVendidoDTO> resultado = reporteService.obtenerProductosMasVendidos(inicioNuevo, finNuevo);
        return ResponseEntity.ok(new ApiResponse("Reporte de productos mas vendidos generado", HttpStatus.OK.value(),resultado));
    }
    @GetMapping("/top-clientes")
    public ResponseEntity<ApiResponse<List<TopEntidadDTO>>> obtenerTopClientes(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        if (inicio == null) inicio = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        if (fin == null) fin = LocalDateTime.now();

        List<TopEntidadDTO> resultado = reporteService.obtenerTopClientes(inicio, fin);
        return ResponseEntity.ok(new ApiResponse("Reporte de top clientes generado", HttpStatus.OK.value(),resultado));
    }

    @GetMapping("/top-proveedores")
    public ResponseEntity<ApiResponse<List<TopEntidadDTO>>> obtenerTopProveedores(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        if (inicio == null) inicio = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        if (fin == null) fin = LocalDateTime.now();

        List<TopEntidadDTO> resultado = reporteService.obtenerTopProveedores(inicio, fin);
        return ResponseEntity.ok(new ApiResponse("Reporte de top proveedores generado", HttpStatus.OK.value(),resultado));
    }
    @GetMapping("/utilidades")
    public ResponseEntity<ApiResponse<UtilidadDTO>> obtenerUtilidad(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        if (inicio == null) inicio = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        if (fin == null) fin = LocalDateTime.now();

        UtilidadDTO utilidad = reporteService.obtenerReporteUtilidad(inicio, fin);
        return ResponseEntity.ok(
                new ApiResponse<>("Reporte de utilidades generado correctamente", HttpStatus.OK.value(), utilidad)
        );
    }


}
