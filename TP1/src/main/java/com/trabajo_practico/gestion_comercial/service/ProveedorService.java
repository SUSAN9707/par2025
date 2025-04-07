package com.trabajo_practico.gestion_comercial.service;

import com.trabajo_practico.gestion_comercial.dto.CreateUpdateProveedorDTO;
import com.trabajo_practico.gestion_comercial.dto.ProveedorDTO;
import com.trabajo_practico.gestion_comercial.model.Proveedor;
import com.trabajo_practico.gestion_comercial.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public ProveedorDTO crearProveedor(CreateUpdateProveedorDTO createUpdateProveedorDTO) {
        var proveedor = generateProveedor(createUpdateProveedorDTO);
        return new ProveedorDTO(proveedorRepository.save(proveedor));
    }

    public List<ProveedorDTO> obtenerTodos() {
        return proveedorRepository.findAll().stream().map(ProveedorDTO::new).collect(Collectors.toList());
    }

    public Optional<ProveedorDTO> obtenerPorId(Long id) {
        var proveedor = proveedorRepository.findById(id);
        return proveedor.map(ProveedorDTO::new);
    }

    public ProveedorDTO actualizarProveedor(Long id, CreateUpdateProveedorDTO updateProveedorDTO) {
        var proveedor = generateProveedor(updateProveedorDTO);
        if (proveedorRepository.existsById(id)) {
            proveedor.setId(id);
            return new ProveedorDTO(proveedorRepository.save(proveedor));
        }
        return null;
    }

    public boolean eliminarProveedor(Long id) {
        if (proveedorRepository.existsById(id)) {
            proveedorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Proveedor generateProveedor(CreateUpdateProveedorDTO dto) {
        var proveedor = new Proveedor();
        proveedor.setNombre(dto.getNombre());
        proveedor.setRuc(dto.getRuc());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        proveedor.setDireccion(dto.getDireccion());
        return proveedor;
    }
}
