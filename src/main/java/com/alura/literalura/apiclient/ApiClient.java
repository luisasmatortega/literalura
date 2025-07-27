package com.alura.literalura.apiclient;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class ApiClient
{
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    public String getData(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("API request failed: " + e.getMessage());
        }
    }
}
