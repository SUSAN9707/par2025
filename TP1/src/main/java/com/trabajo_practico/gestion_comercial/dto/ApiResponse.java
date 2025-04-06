package com.trabajo_practico.gestion_comercial.dto;

public class ApiResponse<T> {
    private String mensaje;
    private int status;
    private T data;

    public ApiResponse(String mensaje, int status, T data) {
        this.mensaje = mensaje;
        this.status = status;
        this.data = data;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }
}
