package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.models.DadosSeries;
import br.com.alura.screenmatch.services.ConsumoAPI;
import br.com.alura.screenmatch.services.ConverteDados;
import java.util.ArrayList;
import br.com.alura.screenmatch.models.*;

import java.util.List;
import java.util.Scanner;

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
    }
}
