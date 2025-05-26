package com.trabajo_practico.gestion_comercial.dto;

public class MovimientoMensualDTO {
    private int mes;
    private long cantidadVentas;
    private double totalVentas;
    private long cantidadCompras;
    private double totalCompras;

    public MovimientoMensualDTO(int mes, long cantidadVentas, double totalVentas, long cantidadCompras, double totalCompras) {
        this.mes = mes;
        this.cantidadVentas = cantidadVentas;
        this.totalVentas = totalVentas;
        this.cantidadCompras = cantidadCompras;
        this.totalCompras = totalCompras;
    }

    // Getters y Setters
    public int getMes() { return mes; }
    public void setMes(int mes) { this.mes = mes; }

    public long getCantidadVentas() { return cantidadVentas; }
    public void setCantidadVentas(long cantidadVentas) { this.cantidadVentas = cantidadVentas; }

    public double getTotalVentas() { return totalVentas; }
    public void setTotalVentas(double totalVentas) { this.totalVentas = totalVentas; }

    public long getCantidadCompras() { return cantidadCompras; }
    public void setCantidadCompras(long cantidadCompras) { this.cantidadCompras = cantidadCompras; }

    public double getTotalCompras() { return totalCompras; }
    public void setTotalCompras(double totalCompras) { this.totalCompras = totalCompras; }
}
