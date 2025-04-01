package com.trabajo_practico.gestion_comercial.controller;

import com.trabajo_practico.gestion_comercial.model.Reporte;
import com.trabajo_practico.gestion_comercial.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/${api.version}/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reportesService;

    @GetMapping
    public List<Reporte> getAllReportes() {
        return reportesService.getAllReportes();
    }

    @GetMapping("/{id}")
    public Optional<Reporte> getReporteById(@PathVariable Long id) {
        return reportesService.getReporteById(id);
    }

    @PostMapping
    public Reporte createReporte(@RequestBody Reporte reporte) {
        return reportesService.createReporte(reporte);
    }

    @PutMapping("/{id}")
    public Reporte updateReporte(@PathVariable Long id, @RequestBody Reporte reporte) {
        return reportesService.updateReporte(id, reporte);
    }

    @DeleteMapping("/{id}")
    public boolean deleteReporte(@PathVariable Long id) {
        return reportesService.deleteReporte(id);
    }
}
