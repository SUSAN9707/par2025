package com.trabajo_practico.gestion_comercial.dto;

public class CompraConFacturaDTO {

    private CreateUpdateCompraDTO compra;
    private CreateFacturaDTO factura; // puede venir null

    public CreateUpdateCompraDTO getCompra() {
        return compra;
    }

    public void setCompra(CreateUpdateCompraDTO compra) {
        this.compra = compra;
    }

    public CreateFacturaDTO getFactura() {
        return factura;
    }

    public void setFactura(CreateFacturaDTO factura) {
        this.factura = factura;
    }
}
