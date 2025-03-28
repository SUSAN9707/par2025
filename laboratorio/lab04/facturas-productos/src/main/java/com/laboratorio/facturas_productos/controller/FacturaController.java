package com.laboratorio.facturas_productos.controller;

import com.laboratorio.facturas_productos.model.Factura;
import com.laboratorio.facturas_productos.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping
    public List<Factura> getAllFacturas() {
        return facturaService.getAllFacturas();
    }

    @GetMapping("/{id}")
    public Optional<Factura> getFacturaById(@PathVariable Long id) {
        return facturaService.getFacturaById(id);
    }

    @PostMapping
    public Factura createFactura(@RequestBody Factura factura) {
        return facturaService.createFactura(factura);
    }

    @PutMapping("/{id}")
    public Factura updateFactura(@PathVariable Long id, @RequestBody Factura factura) {
        return facturaService.updateFactura(id, factura);
    }

    @DeleteMapping("/{id}")
    public boolean deleteFactura(@PathVariable Long id) {
        return facturaService.deleteFactura(id);
    }
}
