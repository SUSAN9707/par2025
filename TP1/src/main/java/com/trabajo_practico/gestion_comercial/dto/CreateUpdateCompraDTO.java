package com.trabajo_practico.gestion_comercial.dto;

public class CreateUpdateCompraDTO {

    private Long productoId;

    private Integer cantidad;
    private Double precioUnitario;
    private Double totalPorArticulo;

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }



    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Double getTotalPorArticulo() {
        return totalPorArticulo;
    }

    public void setTotalPorArticulo(Double totalPorArticulo) {
        this.totalPorArticulo = totalPorArticulo;
    }
}
