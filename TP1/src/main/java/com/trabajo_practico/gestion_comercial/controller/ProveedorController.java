package com.trabajo_practico.gestion_comercial.controller;

import com.trabajo_practico.gestion_comercial.model.Proveedor;
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
    private ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Proveedor>>> obtenerProveedores() {
        List<Proveedor> proveedores = proveedorService.listarProveedores();
        return ResponseEntity.ok(new ApiResponse<>("Proveedores obtenidos correctamente", HttpStatus.OK.value(), proveedores));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Proveedor>> obtenerProveedorPorId(@PathVariable Long id) {
        Optional<Proveedor> proveedor = proveedorService.obtenerProveedorPorId(id);
        return proveedor
                .map(p -> ResponseEntity.ok(new ApiResponse<>("Proveedor encontrado", HttpStatus.OK.value(), p)))
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor con ID " + id + " no encontrado"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Proveedor>> crearProveedor(@RequestBody Proveedor proveedor) {
        Proveedor nuevoProveedor = proveedorService.crearProveedor(proveedor);
        return ResponseEntity.ok(new ApiResponse<>("Proveedor creado exitosamente", HttpStatus.OK.value(), nuevoProveedor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Proveedor>> actualizarProveedor(@PathVariable Long id, @RequestBody Proveedor proveedorActualizado) {
        Proveedor proveedor = proveedorService.actualizarProveedor(id, proveedorActualizado);
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
