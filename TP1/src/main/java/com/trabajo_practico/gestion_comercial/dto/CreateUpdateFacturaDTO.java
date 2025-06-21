package com.trabajo_practico.gestion_comercial.dto;


import java.util.List;

public class CreateUpdateFacturaDTO {

    private String numero;
    private String cliente;
    private double total;

    public List<Long> getReferencias() {
        return referencias;
    }

    public void setReferencias(List<Long> referencias) {
        this.referencias = referencias;
    }

    private List<Long> referencias;
    public long getIdClienteProv() {
        return idClienteProv;
    }

    public String getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(String tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    private String tipoFactura;
    public void setIdClienteProv(long idClienteProv) {
        this.idClienteProv = idClienteProv;
    }

    private long idClienteProv;


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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
