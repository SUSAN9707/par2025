package com.trabajo_practico.gestion_comercial.dto;

public class UtilidadDTO {
    private double totalVentas;
    private double totalCompras;
    private double utilidad;

    public UtilidadDTO(double totalVentas, double totalCompras) {
        this.totalVentas = totalVentas;
        this.totalCompras = totalCompras;
        this.utilidad = totalVentas - totalCompras;
    }

    // Getters y setters

    public double getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(double totalVentas) {
        this.totalVentas = totalVentas;
    }

    public double getTotalCompras() {
        return totalCompras;
    }

    public void setTotalCompras(double totalCompras) {
        this.totalCompras = totalCompras;
    }

    public double getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(double utilidad) {
        this.utilidad = utilidad;
    }
}

