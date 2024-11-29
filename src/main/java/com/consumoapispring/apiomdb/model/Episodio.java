package com.consumoapispring.apiomdb.model;

import com.consumoapispring.apiomdb.dtos.DatosEpisodio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Episodio {
    private Integer temporada;
    private String titulo;
    private LocalDate fechaDeLanzamiento;
    private Integer numeroEpisodio;
    private Double duracion;
    private Double valoracion;

    public Episodio(String numeroTemporada, DatosEpisodio e) {
        setTemporada(Integer.valueOf(numeroTemporada));
        setTitulo(e.titulo());
        try {
            setFechaDeLanzamiento(LocalDate.parse(e.fechaLanzamiento()));
        } catch (DateTimeParseException exc) {
            setFechaDeLanzamiento(null);
        }
        setNumeroEpisodio(Integer.valueOf(e.numeroEpisodio()));
        try {
            setDuracion(Double.valueOf(e.tiempoDuracion()));
        } catch (NullPointerException e1) {
            setDuracion(0.0);
        }
        try {
            setValoracion(Double.valueOf(e.valoracion()));
        } catch (NumberFormatException e2) {
            setValoracion(0.0);
        }

    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return
                "temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", fechaDeLanzamiento=" + fechaDeLanzamiento.format(dtf) +
                ", numeroEpisodio=" + numeroEpisodio +
                ", duracion=" + duracion +
                ", valoracion=" + valoracion;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getFechaDeLanzamiento() {
        return fechaDeLanzamiento;
    }

    public void setFechaDeLanzamiento(LocalDate fechaDeLanzamiento) {
        this.fechaDeLanzamiento = fechaDeLanzamiento;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public Double getDuracion() {
        return duracion;
    }

    public void setDuracion(Double duracion) {
        this.duracion = duracion;
    }

    public Double getValoracion() {
        return valoracion;
    }

    public void setValoracion(Double valoracion) {
        this.valoracion = valoracion;
    }
}
