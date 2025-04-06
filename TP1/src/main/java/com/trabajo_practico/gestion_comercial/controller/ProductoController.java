package com.trabajo_practico.gestion_comercial.controller;

import com.trabajo_practico.gestion_comercial.model.Producto;
import com.trabajo_practico.gestion_comercial.service.ProductoService;
import com.trabajo_practico.gestion_comercial.dto.ApiResponse;
import com.trabajo_practico.gestion_comercial.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/${api.version}/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Producto>>> getAllProductos() {
        List<Producto> productos = productoService.getAllProductos();
        return ResponseEntity.ok(new ApiResponse<>("Productos obtenidos correctamente", HttpStatus.OK.value(), productos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Producto>> getProductoById(@PathVariable Long id) {
        Optional<Producto> producto = productoService.getProductoById(id);
        return producto
                .map(p -> ResponseEntity.ok(new ApiResponse<>("Producto encontrado", HttpStatus.OK.value(), p)))
                .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " + id + " no encontrado"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Producto>> createProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.createProducto(producto);
        return ResponseEntity.ok(new ApiResponse<>("Producto creado exitosamente", HttpStatus.OK.value(), nuevoProducto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Producto>> updateProducto(@PathVariable Long id, @RequestBody Producto productoActualizado) {
        Producto producto = productoService.updateProducto(id, productoActualizado);
        if (producto == null) {
            throw new ResourceNotFoundException("Producto con ID " + id + " no encontrado");
        }
        return ResponseEntity.ok(new ApiResponse<>("Producto actualizado correctamente", HttpStatus.OK.value(), producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProducto(@PathVariable Long id) {
        boolean eliminado = productoService.deleteProducto(id);
        if (!eliminado) {
            throw new ResourceNotFoundException("Producto con ID " + id + " no encontrado");
        }
        return ResponseEntity.ok(new ApiResponse<>("Producto eliminado correctamente", HttpStatus.OK.value(), null));
    }
}
