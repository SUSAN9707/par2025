package com.trabajo_practico.gestion_comercial.dto;

public class VentaConFacturaDTO {

    private CreateUpdateVentaDTO venta;
    private CreateFacturaDTO factura; // puede venir null

    public CreateUpdateVentaDTO getVenta() {
        return venta;
    }

    public void setVenta(CreateUpdateVentaDTO venta) {
        this.venta = venta;
    }
    @Override
    public String toString() {
        return "VentaConFacturaDTO{" +
                "venta=" + (venta != null ? venta.toString() : "null") +
                ", factura=" + (factura != null ? factura.toString() : "null") +
                '}';
    }

    public CreateFacturaDTO getFactura() {
        return factura;
    }

    public void setFactura(CreateFacturaDTO factura) {
        this.factura = factura;
    }
}
