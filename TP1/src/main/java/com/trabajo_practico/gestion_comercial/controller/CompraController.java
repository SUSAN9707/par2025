package com.trabajo_practico.gestion_comercial.controller;

import com.trabajo_practico.gestion_comercial.dto.ApiResponse;
import com.trabajo_practico.gestion_comercial.dto.CompraConFacturaDTO;
import com.trabajo_practico.gestion_comercial.dto.CompraDTO;
import com.trabajo_practico.gestion_comercial.dto.CreateUpdateCompraDTO;
import com.trabajo_practico.gestion_comercial.exception.ResourceNotFoundException;
import com.trabajo_practico.gestion_comercial.service.CompraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/${api.version}/compras")
public class CompraController {

    @Autowired
    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping
    public ResponseEntity<CompraDTO> crearCompra(@RequestBody CompraConFacturaDTO dto) {
        return ResponseEntity.ok(compraService.crearCompra(dto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CompraDTO>> actualizarCompra(@PathVariable Long id, @RequestBody CreateUpdateCompraDTO dto) {
        var compra = compraService.actualizarCompra(id, dto);
        if (compra == null) {
            throw new ResourceNotFoundException("Compra con ID " + id + " no encontrada");
        }
        return ResponseEntity.ok(new ApiResponse<>("Compra actualizada correctamente", HttpStatus.OK.value(), compra));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<CompraDTO>>> obtenerTodas() {
        var compras = compraService.obtenerTodos();
        return ResponseEntity.ok(new ApiResponse<>("Compras obtenidas correctamente", HttpStatus.OK.value(), compras));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompraDTO>> obtenerPorId(@PathVariable Long id) {
        Optional<CompraDTO> compra = compraService.obtenerPorId(id);
        return compra
                .map(c -> ResponseEntity.ok(new ApiResponse<>("Compra encontrada", HttpStatus.OK.value(), c)))
                .orElseThrow(() -> new ResourceNotFoundException("Compra con ID " + id + " no encontrada"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCompra(@PathVariable Long id) {
        boolean eliminado = compraService.eliminarCompra(id);
        if (!eliminado) {
            throw new ResourceNotFoundException("Compra con ID " + id + " no encontrada");
        }
        return ResponseEntity.ok(new ApiResponse<>("Compra eliminada correctamente", HttpStatus.OK.value(), null));
    }
}
