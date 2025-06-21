package com.trabajo_practico.gestion_comercial.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    private int cantidad;

    private LocalDateTime fecha;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(name = "total_por_articulo", nullable = false)
    private Double totalPorArticulo;

    public Venta() {}

    public Venta(Producto producto, Factura factura, int cantidad, LocalDateTime fecha, Double precioUnitario, Double totalPorArticulo) {
        this.producto = producto;
        this.factura = factura;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.precioUnitario = precioUnitario;
        this.totalPorArticulo = totalPorArticulo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }

    public Double getTotalPorArticulo() { return totalPorArticulo; }
    public void setTotalPorArticulo(Double totalPorArticulo) { this.totalPorArticulo = totalPorArticulo; }
}
