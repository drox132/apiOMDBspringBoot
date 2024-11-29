package com.consumoapispring.apiomdb.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosEpisodio(
        @JsonAlias("Title")
        String titulo,
        @JsonAlias("Released")
        String fechaLanzamiento,
        //@JsonAlias("Season")
        //String numeroTemporada,
        @JsonAlias("Episode")
        String numeroEpisodio,
        @JsonAlias("Runtime")
        String tiempoDuracion,
        @JsonAlias("imdbRating")
        String valoracion
        //@JsonAlias("Type")
        //,String tipoContenido


) {
}
