package com.trabajo_practico.gestion_comercial.dto;

import java.time.LocalDateTime;

public class MovimientoReporteDTO {
    private String producto;
    private int cantidad;
    private double precioUnitario;
    private double total;
    private LocalDateTime fecha;

    public MovimientoReporteDTO(String producto, int cantidad, double precioUnitario, double total, LocalDateTime fecha) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.total = total;
        this.fecha = fecha;
    }

    // Getters y setters

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
