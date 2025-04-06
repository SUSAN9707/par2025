package com.trabajo_practico.gestion_comercial.dto;

import com.trabajo_practico.gestion_comercial.model.Reporte;

import java.util.Date;

public class ReporteDTO {
    public ReporteDTO(Reporte reporte) {
        setId(reporte.getId());
        setDescripcion(reporte.getDescripcion());
        setFechaHasta(reporte.getFechaHasta());
        setFechaDesde(reporte.getFechaDesde());
        setIdProductos(reporte.getIdProductos());
        setTipoReporte(reporte.getTipoReporte());
        setMontoTotal(reporte.getMontoTotal());
    }
    private Long id;
    private Double montoTotal;
    private String descripcion;
    private String idProductos;
    private String tipoReporte;
    private Date fechaDesde;
    private Date fechaHasta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getIdProductos() {
        return idProductos;
    }

    public void setIdProductos(String idProductos) {
        this.idProductos = idProductos;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }
}
