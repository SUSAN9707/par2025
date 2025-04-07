package com.trabajo_practico.gestion_comercial.controller;

import com.trabajo_practico.gestion_comercial.dto.CreateUpdateProductoDTO;
import com.trabajo_practico.gestion_comercial.dto.ProductoDTO;

import com.trabajo_practico.gestion_comercial.service.ProductoService;
import com.trabajo_practico.gestion_comercial.dto.ApiResponse;
import com.trabajo_practico.gestion_comercial.exception.ResourceNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/${api.version}/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductoDTO>> crearProducto(@RequestBody CreateUpdateProductoDTO producto) {
        var nuevoProducto = productoService.crearProducto(producto);
        return ResponseEntity.ok(new ApiResponse<>("Producto creado exitosamente", HttpStatus.OK.value(), nuevoProducto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> obtenerTodos() {
        var productos = productoService.obtenerTodos();
        return ResponseEntity.ok(new ApiResponse<>("Productos obtenidos correctamente", HttpStatus.OK.value(), productos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> obtenerPorId(@PathVariable Long id) {
        Optional<ProductoDTO> producto = productoService.obtenerPorId(id);
        return producto
                .map(p -> ResponseEntity.ok(new ApiResponse<>("Producto encontrado", HttpStatus.OK.value(), p)))
                .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " + id + " no encontrado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> actualizarProducto(@PathVariable Long id, @RequestBody CreateUpdateProductoDTO productoActualizado) {
        var producto = productoService.actualizarProducto(id, productoActualizado);
        if (producto == null) {
            throw new ResourceNotFoundException("Producto con ID " + id + " no encontrado");
        }
        return ResponseEntity.ok(new ApiResponse<>("Producto actualizado correctamente", HttpStatus.OK.value(), producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarProducto(@PathVariable Long id) {
        boolean eliminado = productoService.eliminarProducto(id);
        if (!eliminado) {
            throw new ResourceNotFoundException("Producto con ID " + id + " no encontrado");
        }
        return ResponseEntity.ok(new ApiResponse<>("Producto eliminado correctamente", HttpStatus.OK.value(), null));
    }
}
