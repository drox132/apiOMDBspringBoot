package com.consumoapispring.apiomdb;

import com.consumoapispring.apiomdb.dtos.DatosTemporadas;
import com.consumoapispring.apiomdb.model.Episodio;
import com.consumoapispring.apiomdb.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@SpringBootApplication
public class ApiomdbApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ApiomdbApplication.class, args);
	}

	@Override
	public void run(String... args)  {
		Principal principal = new Principal();
		List<DatosTemporadas> datosTemporadasDTO = principal.obtenerDatosTemporadasDTO();
		datosTemporadasDTO.forEach(System.out::println);
//		List<DatosEpisodio> datosEpisodiosDTO = principal.obtenerDatosEpisodioDTO();
//		datosEpisodiosDTO.forEach(System.out::println);
//		List<Episodio> listaEpisodiosObjeto = principal.obtenerListaObjetoEpisodio();
//		listaEpisodiosObjeto.forEach(System.out::println);
//		List<Episodio> episodiosTop5 = principal.obtenerTop5EpisodiosMejorValorados();
//		episodiosTop5.forEach(System.out::println);
//		List<Episodio> listaEpisodiosObjetoDesdeFechaLanzamiento = principal.filtrandoEpisodiosFechaLanzamiento();
//		listaEpisodiosObjetoDesdeFechaLanzamiento.forEach(System.out::println);

//		List<Episodio> listaObjetoEpisodioPorFragmentoPalabra = principal.obtenerListaObjetoEpisodioPorFragmentoPalabra();
//		listaObjetoEpisodioPorFragmentoPalabra.forEach(System.out::println);

		//IMPRIMIMOS LAS EVALUACIONES PROMEDIOS POR  CADA TEMPORADA
		//principal.evaluacionPorTemporada();

		//OBTENEMOS ESTADISTICAS DE LAS TEMPORADAS
		//principal.obtenerEstadisticasTemporadas();

	}
}
