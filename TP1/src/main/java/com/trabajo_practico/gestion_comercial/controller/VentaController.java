package com.trabajo_practico.gestion_comercial.controller;

import com.trabajo_practico.gestion_comercial.dto.ApiResponse;
import com.trabajo_practico.gestion_comercial.dto.VentaDTO;
import com.trabajo_practico.gestion_comercial.dto.CreateUpdateVentaDTO;
import com.trabajo_practico.gestion_comercial.exception.ResourceNotFoundException;
import com.trabajo_practico.gestion_comercial.service.VentaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/${api.version}/ventas")
public class VentaController {

    @Autowired
    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VentaDTO>> crearVenta(@RequestBody CreateUpdateVentaDTO venta) {
        var nuevaVenta = ventaService.crearVenta(venta);
        return ResponseEntity.ok(new ApiResponse<>("Venta registrada exitosamente", HttpStatus.OK.value(), nuevaVenta));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VentaDTO>>> obtenerTodas() {
        var ventas = ventaService.obtenerTodos();
        return ResponseEntity.ok(new ApiResponse<>("Ventas obtenidas correctamente", HttpStatus.OK.value(), ventas));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VentaDTO>> actualizarVenta(@PathVariable Long id, @RequestBody CreateUpdateVentaDTO ventaActualizada) {
        var venta = ventaService.actualizarVenta(id, ventaActualizada);
        if (venta == null) {
            throw new ResourceNotFoundException("Venta con ID " + id + " no encontrada");
        }
        return ResponseEntity.ok(new ApiResponse<>("Venta actualizada correctamente", HttpStatus.OK.value(), venta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VentaDTO>> obtenerPorId(@PathVariable Long id) {
        Optional<VentaDTO> venta = ventaService.obtenerPorId(id);
        return venta
                .map(v -> ResponseEntity.ok(new ApiResponse<>("Venta encontrada", HttpStatus.OK.value(), v)))
                .orElseThrow(() -> new ResourceNotFoundException("Venta con ID " + id + " no encontrada"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarVenta(@PathVariable Long id) {
        boolean eliminado = ventaService.eliminarVenta(id);
        if (!eliminado) {
            throw new ResourceNotFoundException("Venta con ID " + id + " no encontrada");
        }
        return ResponseEntity.ok(new ApiResponse<>("Venta eliminada correctamente", HttpStatus.OK.value(), null));
    }
}
