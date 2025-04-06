package com.trabajo_practico.gestion_comercial.service;

import com.trabajo_practico.gestion_comercial.dto.CreateUpdateReporteDTO;
import com.trabajo_practico.gestion_comercial.dto.ReporteDTO;
import com.trabajo_practico.gestion_comercial.model.Reporte;
import com.trabajo_practico.gestion_comercial.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reportesRepository;

    public List<ReporteDTO> getAllReportes() {
        return reportesRepository.findAll().stream().map(ReporteDTO::new).collect(Collectors.toList());
    }

    public Optional<ReporteDTO> getReporteById(Long id) {
        var reporte = reportesRepository.findById(id);
        return reporte.map(ReporteDTO::new);
    }

    public ReporteDTO createReporte(CreateUpdateReporteDTO createUpdateReporteDTO) {
        var reporte = generateReporte(createUpdateReporteDTO);
        return new ReporteDTO(reportesRepository.save(reporte));
    }

    public ReporteDTO updateReporte(Long id, CreateUpdateReporteDTO updateReporteDTO) {
        var reporte = generateReporte(updateReporteDTO);
        if (reportesRepository.existsById(id)) {
            reporte.setId(id);
            return new ReporteDTO(reportesRepository.save(reporte));
        }
        return null;
    }

    public boolean deleteReporte(Long id) {
        if (reportesRepository.existsById(id)) {
            reportesRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Reporte generateReporte(CreateUpdateReporteDTO updateReporteDTO) {
        var reporte = new Reporte();
        reporte.setTipoReporte(updateReporteDTO.getTipoReporte());
        reporte.setFechaDesde(updateReporteDTO.getFechaDesde());
        reporte.setFechaHasta(updateReporteDTO.getFechaHasta());
        reporte.setTipoReporte(updateReporteDTO.getTipoReporte());
        reporte.setMontoTotal(updateReporteDTO.getMontoTotal());
        reporte.setIdProductos(updateReporteDTO.getIdProductos());
        reporte.setDescripcion(updateReporteDTO.getDescripcion());
        return reporte;
    }
}
