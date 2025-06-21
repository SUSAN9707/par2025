package com.trabajo_practico.gestion_comercial.dto;

public class CreateFacturaDTO {
    private String numero;
    private String cliente;
    private Double total;
    private Long idClienteProv;
    private String tipoFactura;
    private String formaPago;

    // Getters y setters...

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
    @Override
    public String toString() {
        return "CreateFacturaDTO{" +
                "numero='" + numero + '\'' +
                ", cliente='" + cliente + '\'' +
                ", total=" + total +
                ", idClienteProv=" + idClienteProv +
                ", tipoFactura='" + tipoFactura + '\'' +
                ", formaPago='" + formaPago + '\'' +
                '}';
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
}

