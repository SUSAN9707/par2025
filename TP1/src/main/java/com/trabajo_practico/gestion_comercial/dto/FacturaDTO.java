package com.trabajo_practico.gestion_comercial.dto;

import com.trabajo_practico.gestion_comercial.model.EstadoFactura;
import com.trabajo_practico.gestion_comercial.model.Factura;

import java.time.LocalDateTime;

public class FacturaDTO {

    private Long id;
    private String numero;
    private double total;
    private long idClienteProv;
    private String tipoFactura;
    private EstadoFactura estado;
    private LocalDateTime fecha;
    private String formaPago;

    public FacturaDTO(Factura factura) {
        this.id = factura.getId();
        this.numero = factura.getNumero();
        this.total = factura.getTotal();
        this.idClienteProv = factura.getIdClienteProv();
        this.tipoFactura = factura.getTipoFactura();
        this.estado = factura.getEstado();
        this.fecha = factura.getFecha();
        this.formaPago = factura.getFormaPago();
    }

    // Getters y setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }



    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public long getIdClienteProv() { return idClienteProv; }
    public void setIdClienteProv(long idClienteProv) { this.idClienteProv = idClienteProv; }

    public String getTipoFactura() { return tipoFactura; }
    public void setTipoFactura(String tipoFactura) { this.tipoFactura = tipoFactura; }

    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getFormaPago() { return formaPago; }
    public void setFormaPago(String formaPago) { this.formaPago = formaPago; }
}
