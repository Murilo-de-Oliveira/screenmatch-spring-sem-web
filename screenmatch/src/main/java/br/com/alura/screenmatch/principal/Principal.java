package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.models.DadosSeries;
import br.com.alura.screenmatch.services.ConsumoAPI;
import br.com.alura.screenmatch.services.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import br.com.alura.screenmatch.models.*;

import java.util.stream.Collectors;

public class Principal {
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados converteDados = new ConverteDados();
    private Scanner leitura = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String APIKEY = "&apikey=f92f1e86";
    public void exibeMenu(){
        System.out.print("Digite o nome da Série para busca: ");
        var nomeSerie = leitura.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + APIKEY);
        DadosSeries dadosSeries = converteDados.obterDados(json, DadosSeries.class);
        System.out.println(dadosSeries);

        List<DadosTemporadas> temporadas = new ArrayList<>();

		for(int i = 1; i <= dadosSeries.temporadas(); i++){
			json = consumoAPI.obterDados("https://www.omdbapi.com/?t=" + nomeSerie.replace(" ", "+") + "&season=" + i + "&apikey=f92f1e86");
			DadosTemporadas dadosTemporadas = converteDados.obterDados(json,DadosTemporadas.class);
			temporadas.add(dadosTemporadas);
		}
		temporadas.forEach(System.out::println);
        //https://www.omdbapi.com/?t=gilmore+girls&apikey=f92f1e86

//        for(int i = 0; i < dadosSeries.temporadas(); i++){
//            List<DadosEpisodios> episodiosTemporada = temporadas.get(i).episodios();
//            for(int j = 0; j < episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        //mesma coisa que os fors na parte de cima
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
        //funções lambda: (parâmetro) -> expressão

//        Streams são uma abstração que permite processar sequências de elementos de forma
//        declarativa e eficiente. Elas são usadas principalmente para realizar operações
//        em coleções de dados, como listas, conjuntos e mapas.
//        List<String> nomes = Arrays.asList("Muris","Nico","Felipe","Amanda");
//
//        nomes.stream()
//                .sorted() //ordena a lista - operação intermediária
//                .limit(3) //limita a 3
//                .filter(n -> n.startsWith("M")) //filtra para mostrar somente o que começar em M
//                .map(n -> n.toUpperCase()) //ou map(String::toUpperCase()), coloca tudo em letra maiúscula
//                .forEach(System.out::println); //imprime a lista - operação final
//        //permite uma série de operações encadeadas

        List<DadosEpisodios> dadosEpisodios = temporadas.stream()
                .flatMap(temporada -> temporada.episodios().stream())
                .collect(Collectors.toList()); //cria uma lista !mutável!
                //.toList(); //cria uma lista !imutável!

//        System.out.println("\nTOP 5 EPISÓDIOS:");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primeiro filtro (N/A): " + e))
//                .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
//                .peek(e -> System.out.println("Ordenação: " + e))
//                .limit(5)
//                .peek(e -> System.out.println("Limite: " + e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Mapeamento" + e))
//                .forEach(System.out::println);

        System.out.println();
        List<Episodio> episodios = temporadas.stream()
                .flatMap(temporada -> temporada.episodios().stream()
                        .map(d -> new Episodio(temporada.numero(),d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("Digite um trecho do título do episódio: ");
        var trechoTitulo = leitura.nextLine();
        var episodioBuscado = episodios.stream() //valor optional é como um container, que pode ou não ser nulo
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst(); //operação final
        if (episodioBuscado.isPresent()){
            System.out.println("Episódio: " + episodioBuscado.get().getTitulo());
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
        }
        else {
            System.out.println("Episódio não encontrado");
        }
//
//        System.out.println("A partir de que ano você deseja ver os episódios: ");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getDataDeLancamento().isAfter(dataBusca) && e.getDataDeLancamento() != null)
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                " Episódio: " + e.getNumeroEpisodio() +
//                                " Título do episódio: " + e.getTitulo() +
//                                " Data de Lançamento: " + e.getDataDeLancamento().format(formatador)
//                ));

        Map<Integer,Double> avaliacoesTemporadas = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacoesTemporadas);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0)
                .collect(Collectors.summarizingDouble(e -> e.getAvaliacao()));
        System.out.println("Média: " + est.getAverage());
        System.out.println("Menor avaliado: " + est.getMin());
        System.out.println("Maior avaliado: " + est.getMax());
        System.out.println("Episódios avaliados: " + est.getCount());
    }
}
