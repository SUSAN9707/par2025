package com.trabajo_practico.gestion_comercial.dto;

public class TopEntidadDTO {
    private String nombre;
    private Long id;
    private Double total;

    public TopEntidadDTO(String nombre, Long id, Double total) {
        this.nombre = nombre;
        this.id = id;
        this.total = total;
    }

    // Getters y setters...

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
