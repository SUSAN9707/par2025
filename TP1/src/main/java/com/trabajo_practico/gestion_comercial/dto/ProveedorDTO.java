package com.trabajo_practico.gestion_comercial.dto;

import com.trabajo_practico.gestion_comercial.model.Proveedor;

public class ProveedorDTO {
    public Long getId() {
        return id;
    }

    public ProveedorDTO(Proveedor proveedor) {
        setId(proveedor.getId());
        setNombre(proveedor.getNombre());
        setRuc(proveedor.getRuc());
        setTelefono(proveedor.getTelefono());
        setEmail(proveedor.getEmail());
        setDireccion(proveedor.getDireccion());
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;
    private String nombre;

    private String ruc;

    private String telefono;
    private String email;
    private String direccion;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
