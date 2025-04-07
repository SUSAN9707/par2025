package com.trabajo_practico.gestion_comercial.controller;

import com.trabajo_practico.gestion_comercial.dto.CreateUpdateFacturaDTO;
import com.trabajo_practico.gestion_comercial.dto.FacturaDTO;
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
    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FacturaDTO>> crearFactura(@RequestBody CreateUpdateFacturaDTO factura) {
        var nuevaFactura = facturaService.crearFactura(factura);
        return ResponseEntity.ok(new ApiResponse<>("Factura creada exitosamente", HttpStatus.OK.value(), nuevaFactura));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FacturaDTO>>> obtenerTodas() {
        var facturas = facturaService.obtenerTodas();
        return ResponseEntity.ok(new ApiResponse<>("Facturas obtenidas correctamente", HttpStatus.OK.value(), facturas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FacturaDTO>> obtenerPorId(@PathVariable Long id) {
        Optional<FacturaDTO> factura = facturaService.obtenerPorId(id);
        return factura
                .map(f -> ResponseEntity.ok(new ApiResponse<>("Factura encontrada", HttpStatus.OK.value(), f)))
                .orElseThrow(() -> new ResourceNotFoundException("Factura con ID " + id + " no encontrada"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FacturaDTO>> actualizarFactura(@PathVariable Long id, @RequestBody CreateUpdateFacturaDTO facturaActualizada) {
        var factura = facturaService.actualizarFactura(id, facturaActualizada);
        if (factura == null) {
            throw new ResourceNotFoundException("Factura con ID " + id + " no encontrada");
        }
        return ResponseEntity.ok(new ApiResponse<>("Factura actualizada correctamente", HttpStatus.OK.value(), factura));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarFactura(@PathVariable Long id) {
        boolean eliminada = facturaService.eliminarFactura(id);
        if (!eliminada) {
            throw new ResourceNotFoundException("Factura con ID " + id + " no encontrada");
        }
        return ResponseEntity.ok(new ApiResponse<>("Factura eliminada correctamente", HttpStatus.OK.value(), null));
    }
}
