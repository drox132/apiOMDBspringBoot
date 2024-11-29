package com.consumoapispring.apiomdb.service;

public interface IConvierteDatos {

    <T> T convertirDatos(String json, Class<T> clase);
}
