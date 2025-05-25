package com.trabajo_practico.gestion_comercial.dto;

public class CreateUpdateVentaDTO {

    private Long productoId;
    private Integer cantidad;
    public static Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        CreateUpdateVentaDTO.montoTotal = montoTotal;
    }

    private static Double montoTotal;

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
}
