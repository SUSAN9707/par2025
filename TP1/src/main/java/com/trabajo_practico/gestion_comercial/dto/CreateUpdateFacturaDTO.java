package com.trabajo_practico.gestion_comercial.dto;

import java.util.List;

public class CreateUpdateFacturaDTO {

    private String numero;

    private Double total; // opcional, se puede calcular igual
    private Long idClienteProv;
    private String tipoFactura;
    private String formaPago;

    // campos para carga de múltiples compras o ventas
    private List<CreateUpdateCompraDTO> compras;
    private List<CreateUpdateVentaDTO> ventas;

    // Getters y Setters

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }



    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Long getIdClienteProv() {
        return idClienteProv;
    }

    public void setIdClienteProv(Long idClienteProv) {
        this.idClienteProv = idClienteProv;
    }

    public String getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(String tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public List<CreateUpdateCompraDTO> getCompras() {
        return compras;
    }

    public void setCompras(List<CreateUpdateCompraDTO> compras) {
        this.compras = compras;
    }

    public List<CreateUpdateVentaDTO> getVentas() {
        return ventas;
    }

    public void setVentas(List<CreateUpdateVentaDTO> ventas) {
        this.ventas = ventas;
    }

    @Override
    public String toString() {
        return "CreateUpdateFacturaDTO{" +
                "numero='" + numero + '\'' +
                ", total=" + total +
                ", idClienteProv=" + idClienteProv +
                ", tipoFactura='" + tipoFactura + '\'' +
                ", formaPago='" + formaPago + '\'' +
                ", compras=" + compras +
                ", ventas=" + ventas +
                '}';
    }
}
