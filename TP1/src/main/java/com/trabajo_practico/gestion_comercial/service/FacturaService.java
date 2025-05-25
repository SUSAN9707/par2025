package com.trabajo_practico.gestion_comercial.service;
import com.trabajo_practico.gestion_comercial.dto.CreateUpdateFacturaDTO;
import com.trabajo_practico.gestion_comercial.dto.FacturaDTO;
import com.trabajo_practico.gestion_comercial.model.EstadoFactura;
import com.trabajo_practico.gestion_comercial.model.Factura;
import com.trabajo_practico.gestion_comercial.model.FacturaDetalle;
import com.trabajo_practico.gestion_comercial.model.TipoFactura;
import com.trabajo_practico.gestion_comercial.repository.ClienteRepository;
import com.trabajo_practico.gestion_comercial.repository.FacturaDetalleRepository;
import com.trabajo_practico.gestion_comercial.repository.FacturaRepository;
import com.trabajo_practico.gestion_comercial.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final ClienteRepository clienteRepository;
    private final ProveedorRepository proveedorRepository;

    @Autowired
    private FacturaDetalleRepository facturaDetalleRepository;

    public FacturaService(FacturaRepository facturaRepository, ClienteRepository clienteRepository, ProveedorRepository proveedorRepository) {
        this.facturaRepository = facturaRepository;
        this.clienteRepository = clienteRepository;
        this.proveedorRepository = proveedorRepository;
    }

    public FacturaDTO crearFactura(CreateUpdateFacturaDTO createUpdateFacturaDTO) {
        TipoFactura tipoFactura;
        try {
            tipoFactura = TipoFactura.valueOf(createUpdateFacturaDTO.getTipoFactura().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Tipo de factura inválido
        }

        Long idClienteProv = createUpdateFacturaDTO.getidClienteProv();

        boolean existe = switch (tipoFactura) {
            case COMPRA -> proveedorRepository.existsById(idClienteProv);
            case VENTA -> clienteRepository.existsById(idClienteProv);
        };

        if (!existe) return null;

        // Crear y guardar factura
        Factura factura = generateFactura(createUpdateFacturaDTO);
        factura = facturaRepository.save(factura); // ya tiene ID

        // Insertar detalles de factura
        List<Long> referencias = createUpdateFacturaDTO.getReferencias(); // compras o ventas
        if (referencias != null && !referencias.isEmpty()) {
            Factura finalFactura = factura;
            List<FacturaDetalle> detalles = referencias.stream().map(idRef -> {
                FacturaDetalle detalle = new FacturaDetalle();
                detalle.setFactura(finalFactura);
                detalle.setReferenciaId(idRef);
                detalle.setTipoFactura(tipoFactura);
                return detalle;
            }).toList();
            facturaDetalleRepository.saveAll(detalles);
        }

        return new FacturaDTO(factura);
    }

    public boolean anularFactura(Long id) {
        Optional<Factura> optionalFactura = facturaRepository.findById(id);
        if (optionalFactura.isEmpty()) return false;

        Factura factura = optionalFactura.get();
        factura.setEstado(EstadoFactura.ANULADA);
        facturaRepository.save(factura);
        return true;
    }



    public List<FacturaDTO> obtenerTodas() {
        return facturaRepository.findAll().stream().map(FacturaDTO::new).collect(Collectors.toList());
    }

    public Optional<FacturaDTO> obtenerPorId(Long id) {
        var factura = facturaRepository.findById(id);
        return factura.map(FacturaDTO::new);
    }

    public FacturaDTO actualizarFactura(Long id, CreateUpdateFacturaDTO updateFacturaDTO) {
        // Verificar si la factura existe
        if (!facturaRepository.existsById(id)) {
            return null; // Factura no encontrada
        }

        // Verificar si existe cliente o proveedor según tipoFactura
        if ("COMPRA".equalsIgnoreCase(updateFacturaDTO.getTipoFactura())) {
            // Para tipo COMPRA, validar proveedor
            if (!proveedorRepository.existsById(updateFacturaDTO.getidClienteProv())) {
                return null; // Proveedor no encontrado
            }
        } else if ("VENTA".equalsIgnoreCase(updateFacturaDTO.getTipoFactura())) {
            // Para tipo VENTA, validar cliente
            if (!clienteRepository.existsById(updateFacturaDTO.getidClienteProv())) {
                return null; // Cliente no encontrado
            }
        } else {
            // Tipo factura no válido, opcional lanzar excepción o retornar null
            return null;
        }

        var factura = generateFactura(updateFacturaDTO);
        factura.setId(id);
        return new FacturaDTO(facturaRepository.save(factura));
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
        factura.setidClienteProv(updateFacturaDTO.getidClienteProv());
        factura.setTipoFactura(updateFacturaDTO.getTipoFactura());
        return factura;
    }
}
