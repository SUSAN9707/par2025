package com.trabajo_practico.gestion_comercial.controller;

import com.trabajo_practico.gestion_comercial.dto.CreateUpdateProveedorDTO;
import com.trabajo_practico.gestion_comercial.dto.ProveedorDTO;
import com.trabajo_practico.gestion_comercial.service.ProveedorService;
import com.trabajo_practico.gestion_comercial.dto.ApiResponse;
import com.trabajo_practico.gestion_comercial.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/${api.version}/proveedores")
public class ProveedorController {


    @Autowired
    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProveedorDTO>> crearProveedor(@RequestBody CreateUpdateProveedorDTO proveedor) {
        var nuevoProveedor = proveedorService.crearProveedor(proveedor);
        return ResponseEntity.ok(new ApiResponse<>("Proveedor creado exitosamente", HttpStatus.OK.value(), nuevoProveedor));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProveedorDTO>>> obtenerTodos() {
        var proveedores = proveedorService.obtenerTodos();
        return ResponseEntity.ok(new ApiResponse<>("Proveedores obtenidos correctamente", HttpStatus.OK.value(), proveedores));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProveedorDTO>> obtenerPorId(@PathVariable Long id) {
        Optional<ProveedorDTO> proveedor = proveedorService.obtenerPorId(id);
        return proveedor
                .map(p -> ResponseEntity.ok(new ApiResponse<>("Proveedor encontrado", HttpStatus.OK.value(), p)))
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor con ID " + id + " no encontrado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProveedorDTO>> actualizarProveedor(@PathVariable Long id, @RequestBody CreateUpdateProveedorDTO proveedorActualizado) {
        var proveedor = proveedorService.actualizarProveedor(id, proveedorActualizado);
        if (proveedor == null) {
            throw new ResourceNotFoundException("Proveedor con ID " + id + " no encontrado");
        }
        return ResponseEntity.ok(new ApiResponse<>("Proveedor actualizado correctamente", HttpStatus.OK.value(), proveedor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarProveedor(@PathVariable Long id) {
        boolean eliminado = proveedorService.eliminarProveedor(id);
        if (!eliminado) {
            throw new ResourceNotFoundException("Proveedor con ID " + id + " no encontrado");
        }
        return ResponseEntity.ok(new ApiResponse<>("Proveedor eliminado correctamente", HttpStatus.OK.value(), null));
    }
}
