package br.com.alura.screenmatch;

import br.com.alura.screenmatch.models.DadosSeries;
import br.com.alura.screenmatch.services.ConsumoAPI;
import br.com.alura.screenmatch.services.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumoAPI consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=friends&apikey=f92f1e86");
		System.out.println(json);
		ConverteDados converteDados = new ConverteDados();
		DadosSeries dadosSeries = converteDados.obterDados(json, DadosSeries.class);
		System.out.println(dadosSeries);
	}
}
