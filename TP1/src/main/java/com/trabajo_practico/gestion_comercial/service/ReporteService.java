package com.trabajo_practico.gestion_comercial.service;

import com.trabajo_practico.gestion_comercial.model.Reporte;
import com.trabajo_practico.gestion_comercial.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reportesRepository;

    public List<Reporte> getAllReportes() {
        return reportesRepository.findAll();
    }

    public Optional<Reporte> getReporteById(Long id) {
        return reportesRepository.findById(id);
    }

    public Reporte createReporte(Reporte reporte) {
        return reportesRepository.save(reporte);
    }

    public Reporte updateReporte(Long id, Reporte reporte) {
        if (reportesRepository.existsById(id)) {
            reporte.setId(id);
            return reportesRepository.save(reporte);
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
}
