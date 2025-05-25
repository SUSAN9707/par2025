package com.trabajo_practico.gestion_comercial.dto;

import com.trabajo_practico.gestion_comercial.model.EstadoFactura;
import com.trabajo_practico.gestion_comercial.model.Factura;

public class FacturaDTO {
    public FacturaDTO(Factura factura) {
        setId(factura.getId());
        setNumero(factura.getNumero());
        setCliente(factura.getCliente());
        setTotal(factura.getTotal());
        setidClienteProv(factura.getidClienteProv());
    }
    private Long id;
    private String numero;
    private String cliente;
    private double total;
    private EstadoFactura estado;

    public EstadoFactura getEstado() {
        return estado;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public long getidClienteProv() {
        return idClienteProv;
    }

    public String getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(String tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    private String tipoFactura;
    public void setidClienteProv(long idClienteProv) {
        this.idClienteProv = idClienteProv;
    }

    private long idClienteProv;


    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
