package com.consumoapispring.apiomdb.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosTemporadas(
        @JsonAlias("Season")
        String numeroTemporada,
        @JsonAlias("Episodes")
        List<DatosEpisodio> episodiosTemporada
) {
}
