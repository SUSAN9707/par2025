package com.trabajo_practico.gestion_comercial.dto;

public class ProductoMasVendidoDTO {
    private String nombreProducto;
    private long cantidadVendida;

    public ProductoMasVendidoDTO(String nombreProducto, long cantidadVendida) {
        this.nombreProducto = nombreProducto;
        this.cantidadVendida = cantidadVendida;
    }

    // Getters y setters...

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public long getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(long cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }
}
