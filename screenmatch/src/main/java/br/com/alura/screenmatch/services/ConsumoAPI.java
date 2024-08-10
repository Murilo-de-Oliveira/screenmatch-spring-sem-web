package br.com.alura.screenmatch.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {
    public String obterDados(String endereco) {
        HttpClient client = HttpClient.newHttpClient(); //cria um HTTP Client
        //O HttpClient é uma classe em Java que permite enviar requisições HTTP para servidores remotos e receber respostas
        HttpRequest request = HttpRequest.newBuilder() //define a requisição HTTP que será enviada ao servidor
                .uri(URI.create(endereco))
                .build();
        HttpResponse<String> response = null; //representa a resposta da requisição HTTP

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString()); //recebe a requisição HTTP gerada pela API
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String json = response.body(); //recebe a resposta no formato de string
        return json; //retorna o corpo da resposta
    }
}
