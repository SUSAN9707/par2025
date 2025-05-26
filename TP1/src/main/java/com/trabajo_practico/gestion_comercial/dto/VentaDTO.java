package com.trabajo_practico.gestion_comercial.dto;

import com.trabajo_practico.gestion_comercial.model.Venta;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VentaDTO {
    public VentaDTO(Venta venta) {
        setId(venta.getId());
        setFecha(venta.getFecha());
        setCantidad(venta.getCantidad());
        setProductoId(venta.getProducto().getId());
        setMontoTotal(venta.getMontoTotal());
    }

    private Long id;
    private LocalDateTime fecha;
    private Integer cantidad;
    private Long productoId;
    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    private Double montoTotal;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
}
