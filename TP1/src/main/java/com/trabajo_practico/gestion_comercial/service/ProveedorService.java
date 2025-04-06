package com.trabajo_practico.gestion_comercial.service;

import com.trabajo_practico.gestion_comercial.model.Proveedor;
import com.trabajo_practico.gestion_comercial.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findAll();
    }

    public Optional<Proveedor> obtenerProveedorPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    public Proveedor crearProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public Proveedor actualizarProveedor(Long id, Proveedor proveedorDetalles) {
        return proveedorRepository.findById(id)
                .map(proveedor -> {
                    proveedor.setNombre(proveedorDetalles.getNombre());
                    proveedor.setRuc(proveedorDetalles.getRuc());
                    proveedor.setTelefono(proveedorDetalles.getTelefono());
                    proveedor.setEmail(proveedorDetalles.getEmail());
                    proveedor.setDireccion(proveedorDetalles.getDireccion());
                    return proveedorRepository.save(proveedor);
                }).orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + id));
    }

    public boolean eliminarProveedor(Long id) {
        proveedorRepository.deleteById(id);
        return false;
    }
}
