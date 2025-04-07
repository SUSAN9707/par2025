package com.trabajo_practico.gestion_comercial.service;
import com.trabajo_practico.gestion_comercial.dto.CreateUpdateFacturaDTO;
import com.trabajo_practico.gestion_comercial.dto.FacturaDTO;
import com.trabajo_practico.gestion_comercial.model.Factura;
import com.trabajo_practico.gestion_comercial.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;

    public FacturaService(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    public FacturaDTO crearFactura(CreateUpdateFacturaDTO createUpdateFacturaDTO) {
        var factura = generateFactura(createUpdateFacturaDTO);
        return new FacturaDTO(facturaRepository.save(factura));
    }

    public List<FacturaDTO> obtenerTodas() {
        return facturaRepository.findAll().stream().map(FacturaDTO::new).collect(Collectors.toList());
    }

    public Optional<FacturaDTO> obtenerPorId(Long id) {
        var factura = facturaRepository.findById(id);
        return factura.map(FacturaDTO::new);
    }

    public FacturaDTO actualizarFactura(Long id, CreateUpdateFacturaDTO updateFacturaDTO) {
        var factura = generateFactura(updateFacturaDTO);
        if (facturaRepository.existsById(id)) {
            factura.setId(id);
            return new FacturaDTO(facturaRepository.save(factura));
        }
        return null;
    }

    public boolean eliminarFactura(Long id) {
        if (facturaRepository.existsById(id)) {
            facturaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Factura generateFactura(CreateUpdateFacturaDTO updateFacturaDTO) {
        var factura = new Factura();
        factura.setNumero(updateFacturaDTO.getNumero());
        factura.setCliente(updateFacturaDTO.getCliente());
        factura.setTotal(updateFacturaDTO.getTotal());
        return factura;
    }
}
