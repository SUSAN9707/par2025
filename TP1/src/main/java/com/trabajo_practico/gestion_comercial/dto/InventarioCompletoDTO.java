package com.trabajo_practico.gestion_comercial.dto;


import java.math.BigDecimal;
import java.util.List;

public class InventarioCompletoDTO {
    private BigDecimal totalInventario;
    private List<InventarioProductoDTO> productos;

    public InventarioCompletoDTO(BigDecimal totalInventario, List<InventarioProductoDTO> productos) {
        this.totalInventario = totalInventario;
        this.productos = productos;
    }

    public BigDecimal getTotalInventario() {
        return totalInventario;
    }

    public List<InventarioProductoDTO> getProductos() {
        return productos;
    }
}
