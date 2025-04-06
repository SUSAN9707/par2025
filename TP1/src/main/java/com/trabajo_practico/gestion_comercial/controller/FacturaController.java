package com.trabajo_practico.gestion_comercial.controller;

import com.trabajo_practico.gestion_comercial.model.Factura;
import com.trabajo_practico.gestion_comercial.service.FacturaService;
import com.trabajo_practico.gestion_comercial.dto.ApiResponse;
import com.trabajo_practico.gestion_comercial.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/${api.version}/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Factura>>> getAllFacturas() {
        List<Factura> facturas = facturaService.getAllFacturas();
        return ResponseEntity.ok(new ApiResponse<>("Facturas obtenidas correctamente", HttpStatus.OK.value(), facturas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Factura>> getFacturaById(@PathVariable Long id) {
        Optional<Factura> factura = facturaService.getFacturaById(id);
        return factura
                .map(f -> ResponseEntity.ok(new ApiResponse<>("Factura encontrada", HttpStatus.OK.value(), f)))
                .orElseThrow(() -> new ResourceNotFoundException("Factura con ID " + id + " no encontrada"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Factura>> createFactura(@RequestBody Factura factura) {
        Factura nueva = facturaService.createFactura(factura);
        return new ResponseEntity<>(new ApiResponse<>("Factura creada correctamente", HttpStatus.OK.value(), nueva), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Factura>> updateFactura(@PathVariable Long id, @RequestBody Factura factura) {
        Factura actualizada = facturaService.updateFactura(id, factura);
        if (actualizada == null) {
            throw new ResourceNotFoundException("Factura con ID " + id + " no encontrada");
        }
        return ResponseEntity.ok(new ApiResponse<>("Factura actualizada", HttpStatus.OK.value(), actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFactura(@PathVariable Long id) {
        boolean eliminado = facturaService.deleteFactura(id);
        if (!eliminado) {
            throw new ResourceNotFoundException("Factura con ID " + id + " no encontrada");
        }
        return ResponseEntity.ok(new ApiResponse<>("Factura eliminada", HttpStatus.OK.value(), null));
    }
}
