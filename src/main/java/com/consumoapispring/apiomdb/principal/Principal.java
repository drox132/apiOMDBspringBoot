package com.consumoapispring.apiomdb.principal;

import com.consumoapispring.apiomdb.dtos.DatosEpisodio;
import com.consumoapispring.apiomdb.dtos.DatosSerie;
import com.consumoapispring.apiomdb.dtos.DatosTemporadas;
import com.consumoapispring.apiomdb.exception.ConsumoApiOMDBException;
import com.consumoapispring.apiomdb.model.Episodio;
import com.consumoapispring.apiomdb.service.ConsumoApi;
import com.consumoapispring.apiomdb.service.ConvierteDatos;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private Scanner entrada = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger(Principal.class);

    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=4fc7c187";

    public List<DatosTemporadas> obtenerDatosTemporadasDTO() {

        logger.debug("se invoco el metodo --> obtenerDatosTemporadasDTO() ");

        //Se pide el nombre de la serie al usuario
        System.out.println("Por favor ingrese el nombre de la Serie");
        var nombreBusqueda = entrada.nextLine();

        var json = consumoApi.obtenerDatos(URL_BASE + nombreBusqueda.replace(" ", "+") + API_KEY);

        logger.info("obtenemos json de la serie" + json);
        //se realiza la busqueda de la Serie en general
        DatosSerie datosSerie = convierteDatos.convertirDatos(json, DatosSerie.class);
        //System.out.println(datosSerie);
        logger.info("obtenemos datosSerie en forma de DTO " +datosSerie);

        //se busca la informacion de todas las temporadas
        List<DatosTemporadas> temporadas = new ArrayList<>();
        try {
            for (int i = 1; i <= datosSerie.totalTemporadas(); i++) {
                json = consumoApi.obtenerDatos(URL_BASE + nombreBusqueda.replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporadas = convierteDatos.convertirDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporadas);
                logger.info(" obtenemos informacion de la temporada " + i + " " + datosTemporadas);
            }
        } catch (NullPointerException e) {
            logger.warn("ha ocurrido un error en la busqueda la serie , es posible que la serie no exista o que no tenga los " +
                    "criterios necesarios para considerarse una serie --> "+ e.getMessage());
            throw new ConsumoApiOMDBException("Es posible que el nombre de la serie no exista " +
                    "o no tenga los parametros para considerarse una serie " + e.getMessage());

        }


        //Muestra informacion de las temporadas

//        temporadas.forEach(e->{
//            System.out.println("los capitulos de la temporada "+ e.numeroTemporada() +" son:");
//            e.episodiosTemporada().forEach(System.out::println);
//        });
        return temporadas;
    }

    public List<DatosEpisodio> obtenerDatosEpisodioDTO() {
        logger.info("se invoco el metodo obtenerDatosEpisodioDTO()");
       List<DatosTemporadas> temporadasDTO = obtenerDatosTemporadasDTO();
        //obteniendo las listas de episodias de todas las temporadas para poder trabajarlos
        logger.info("obteniendo las listas de episodias de todas las temporadas para poder trabajarlos");
        return  temporadasDTO.stream()
                .flatMap(t -> t.episodiosTemporada().stream())
                .collect(Collectors.toList());

    }

    public List<Episodio> obtenerListaObjetoEpisodio() {
        logger.info("Se invoco el metodo --> obtenerListaObjetoEpisodio()");
        List<DatosTemporadas> datosTemporadasDTO = obtenerDatosTemporadasDTO();
        //mapeando los datos del Dto datos episodio a la clase Episodio
        logger.info("mapeando los datos del Dto datos episodio a la clase Episodio");
        return datosTemporadasDTO.stream()
                .flatMap(t -> t.episodiosTemporada().stream()
                        .map(e-> new Episodio(t.numeroTemporada(),e)))
                //.peek(e-> System.out.println("Primer paso seleccionando la lista de episodios "+e))
                //.peek(t-> System.out.println("obteniendo la seleccion de la lista de titulos " + t))
                .toList();
    }

    public List<Episodio> obtenerTop5EpisodiosMejorValorados() {
        logger.info("Se invoco el metodo --> obtenerTop5EpisodiosMejorValorados()");
        //creando un top 5 episodios mejor valorados
        return obtenerListaObjetoEpisodio().stream()
                //.filter(e -> !e.getValoracion().equals(0.0)) //esto ya no es necesario xq cuando creamos el objeto sustituimos esos valores por 0 asi no afectara la ordenacion
                .sorted(Comparator.comparing(Episodio::getValoracion).reversed())
                .limit(5)
                .collect(Collectors.toList());
//        episodios.stream()
//                .filter(e-> !e.valoracion().equalsIgnoreCase("N/A"))
//                .peek(e-> System.out.println("filtrando los episodiosque no tengan valoracion N/A  " + e))
//                .sorted(Comparator.comparing(DatosEpisodio::valoracion).reversed())
//                .peek(e-> System.out.println("ordenando la lista de los episodios  " + e))
//                .limit(5)
//                .peek(e-> System.out.println("Limitando a los 5 mejores episodios valorados " + e))
//                .forEach(System.out::println);
    }

    public List<Episodio> filtrandoEpisodiosFechaLanzamiento() {
        logger.info("se invoco el metodo --> filtrandoEpisodiosFechaLanzamiento() ");
        List<Episodio> listadoObjetoEpisodios = obtenerListaObjetoEpisodio();
        //Filtrando listadoObjetoEpisodios por fecha de lanzamiento

        System.out.println("Por favor ingrese a partir de que año de lanzamiento quiere ver los capitulos ");
        var ano = entrada.nextInt();

        //preparando el formato de fecha con el que se desea ver en las respuestas de pantalla

        var fechaBusqueda = LocalDate.of(ano, 1, 1);
        return listadoObjetoEpisodios.stream()
                .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
                .collect(Collectors.toList());
//                .forEach(e->
//                        System.out.println(
//                                "Temporada: " + e.getTemporada() +
//                                        " Titulo: " + e.getTitulo() +
//                                        " Fecha de Lanzamiento: " + e.getFechaDeLanzamiento().format(dtf) +
//                                        " Numero de Episodio : " +e.getNumeroEpisodio() +
//                                        " Duracion: " + e.getDuracion() +
//                                        " Valoracion: " + e.getValoracion()
//                        ));
        /*
        * La parte del DateTimeFormater que es un patron con el que queremos mostrar la fecha  no lo usaremos
        * en este caso ya que solo podriamos modificar el patron cuando queremos mostrarlo , asi que lo mejor seria modificar el toString
        * de esa forma la salida de la fecha se vera formateada de acuerdo a como deseamos
        *
        */
    }
    public List<Episodio> obtenerListaObjetoEpisodioPorFragmentoPalabra() {
        logger.info("se invoco el metodo --> obtenerListaObjetoEpisodioPorFragmentoPalabra() ");
        List<Episodio> listaObjetoEpisodio = obtenerListaObjetoEpisodio();
        System.out.println("Por favor ingrese el nombre del capitulo que desea ver");
        var capituloBuscado = entrada.nextLine();


        return listaObjetoEpisodio.stream()
                .filter(e -> e.getTitulo().toLowerCase().contains(capituloBuscado.toLowerCase()))
//                      .findFirst()
                .collect(Collectors.toList());

        /*
        * NOTA: EL METODO findfirst() o findAny() se pueden usar para devolver el primer episodio o alguno de los
        * encontrados en mi caso yo quiero que me devuelva una lista de lo que haya encontrado  pero se puede configurar
        * como se desee y esos datos devolveran un optional del objeto que proporciona otros metodos para verificar
        * si viene nulo
        * */



    }
    public void evaluacionPorTemporada () {
        logger.info("se invoco el metodo --> evaluacionPorTemporada() ");
        List<Episodio> episodios = obtenerListaObjetoEpisodio();
        Map<Integer, Double>  evaluacionPorTemporada = episodios.stream()
                .filter(e -> e.getValoracion() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getValoracion)));

        Set<Map.Entry<Integer, Double>> entries = evaluacionPorTemporada.entrySet();
        entries.forEach(e-> System.out.println(
                "Temporada => "+ e.getKey() + " Valoracion => " + e.getValue()
        ));
    }

    public void obtenerEstadisticasTemporadas() {
        logger.info("se invoco el metodo --> obtenerEstadisticasTemporadas() ");
        List<Episodio> episodios = obtenerListaObjetoEpisodio();

        DoubleSummaryStatistics estadisticas = episodios.stream()
                .filter(e -> e.getValoracion() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getValoracion));

        System.out.println(estadisticas);
    }

}





//==============================================================================================
//
//		//OTRA FORMA DE HACER MAS SEGURA SERIA
//		Optional<List<Episodio>>
//				listaObjetoEpisodioPorFragmentoPalabra =
//				Optional.ofNullable(principal.obtenerListaObjetoEpisodioPorFragmentoPalabra())
//						.filter(lista -> !lista.isEmpty());
//
//		if (listaObjetoEpisodioPorFragmentoPalabra.isPresent()) {
//			System.out.println(listaObjetoEpisodioPorFragmentoPalabra);
//			System.out.println("Se encontraron los siguientes resultados para la busqueda");
//			listaObjetoEpisodioPorFragmentoPalabra.get().forEach(System.out::println);
//		}else{
//			System.out.println("No se encontraron episodios para la busqueda");
//		}
/*NOTA:
 * Si necesitas que el Optional esté vacío cuando la lista está vacía, debes verificar explícitamente
 *  si la lista está vacía antes de envolverla en el Optional. Esto se puede hacer de varias formas:
 * Opción 1: Filtrar la lista
 * Puedes usar Optional junto con el método filter para asegurarte de que no se considere "presente"
 * si la lista está vacía:*/