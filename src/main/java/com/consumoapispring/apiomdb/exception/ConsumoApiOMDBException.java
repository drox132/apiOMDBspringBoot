package com.consumoapispring.apiomdb.exception;

public class ConsumoApiOMDBException extends RuntimeException{
    private String mensaje;


    public ConsumoApiOMDBException(String mensaje) {
        setMensaje(mensaje);
    }


    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String getMessage() {
        return mensaje;
    }
}
