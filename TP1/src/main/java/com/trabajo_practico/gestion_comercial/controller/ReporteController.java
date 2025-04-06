package com.trabajo_practico.gestion_comercial.controller;

import com.trabajo_practico.gestion_comercial.dto.CreateUpdateReporteDTO;
import com.trabajo_practico.gestion_comercial.dto.ReporteDTO;
import com.trabajo_practico.gestion_comercial.service.ReporteService;
import com.trabajo_practico.gestion_comercial.dto.ApiResponse;
import com.trabajo_practico.gestion_comercial.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/${api.version}/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reportesService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReporteDTO>>> getAllReportes() {
        var reportes = reportesService.getAllReportes();
        return ResponseEntity.ok(new ApiResponse<>("Reportes obtenidos correctamente", HttpStatus.OK.value(), reportes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReporteDTO>> getReporteById(@PathVariable Long id) {
        Optional<ReporteDTO> reporte = reportesService.getReporteById(id);
        return reporte
                .map(r -> ResponseEntity.ok(new ApiResponse<>("Reporte encontrado", HttpStatus.OK.value(), r)))
                .orElseThrow(() -> new ResourceNotFoundException("Reporte con ID " + id + " no encontrado"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReporteDTO>> createReporte(@RequestBody CreateUpdateReporteDTO reporte) {
        var nuevoReporte = reportesService.createReporte(reporte);
        return ResponseEntity.ok(new ApiResponse<>("Reporte creado exitosamente", HttpStatus.OK.value(), nuevoReporte));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReporteDTO>> updateReporte(@PathVariable Long id, @RequestBody CreateUpdateReporteDTO reporte) {
        var actualizado = reportesService.updateReporte(id, reporte);
        if (actualizado == null) {
            throw new ResourceNotFoundException("Reporte con ID " + id + " no encontrado");
        }
        return ResponseEntity.ok(new ApiResponse<>("Reporte actualizado correctamente", HttpStatus.OK.value(), actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReporte(@PathVariable Long id) {
        boolean eliminado = reportesService.deleteReporte(id);
        if (!eliminado) {
            throw new ResourceNotFoundException("Reporte con ID " + id + " no encontrado");
        }
        return ResponseEntity.ok(new ApiResponse<>("Reporte eliminado correctamente", HttpStatus.OK.value(), null));
    }
}
