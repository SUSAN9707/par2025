package com.trabajo_practico.gestion_comercial.dto;

import com.trabajo_practico.gestion_comercial.model.Venta;

import java.time.LocalDateTime;

public class VentaDTO {

    private Long id;
    private LocalDateTime fecha;
    private Integer cantidad;
    private Long productoId;
    private Long facturaId;
    private Double precioUnitario;
    private Double totalPorArticulo;

    public VentaDTO(Venta venta) {
        this.id = venta.getId();
        this.fecha = venta.getFecha();
        this.cantidad = venta.getCantidad();
        this.productoId = venta.getProducto().getId();
        this.facturaId = venta.getFactura().getId();
        this.precioUnitario = venta.getPrecioUnitario();
        this.totalPorArticulo = venta.getTotalPorArticulo();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Long getFacturaId() { return facturaId; }
    public void setFacturaId(Long facturaId) { this.facturaId = facturaId; }

    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }

    public Double getTotalPorArticulo() { return totalPorArticulo; }
    public void setTotalPorArticulo(Double totalPorArticulo) { this.totalPorArticulo = totalPorArticulo; }
}
