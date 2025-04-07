package com.trabajo_practico.gestion_comercial.dto;

import com.trabajo_practico.gestion_comercial.model.Producto;

public class ProductoDTO {
    public ProductoDTO(Producto producto) {
        setId(producto.getId());
        setNombre(producto.getNombre());
        setPrecio(producto.getPrecio());
        setDescripcion(producto.getDescripcion());
    }
    private Long id;
    private String nombre;
    private double precio;
    private String descripcion;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
