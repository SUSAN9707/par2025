package com.trabajo_practico.gestion_comercial.dto;

import java.math.BigDecimal;

public class InventarioProductoDTO {
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private int stockMinimo;
    private String estadoStock;

    public InventarioProductoDTO(String nombre, String descripcion, double precio, int stock, int stockMinimo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.estadoStock = stock < stockMinimo ? "Crítico" : "Suficiente";
    }

    // Método para obtener el valor total del producto en stock
    public BigDecimal getValorTotalProducto() {
        return BigDecimal.valueOf(precio).multiply(BigDecimal.valueOf(stock));
    }

    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public String getEstadoStock() { return estadoStock; }
    public void setEstadoStock(String estadoStock) { this.estadoStock = estadoStock; }
}
