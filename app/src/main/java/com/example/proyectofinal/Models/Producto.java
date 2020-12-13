package com.example.proyectofinal.Models;

public class Producto {
    private String idProducto;
    private String descripction;
    private String precio;
    private String fechaRegistro;
    private long timeOrder;

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getDescripction() {
        return descripction;
    }

    public void setDescripction(String descripction) {
        this.descripction = descripction;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public long getTimeOrder() {
        return timeOrder;
    }

    public void setTimeOrder(long timeOrder) {
        this.timeOrder = timeOrder;
    }
}
