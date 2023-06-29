package com.example.demo.voting.service;

import com.example.demo.voting.model.Blockchain;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;


@Service
public class PeerNetworkingService {
    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public CompletableFuture<Blockchain> getBlockchainFromPeerAsync(String peerUrl) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(peerUrl + "/blockchain"))
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        return responseFuture.thenApply(response -> {
            try {
                String responseBody = response.body();
                return objectMapper.readValue(responseBody, Blockchain.class);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }



    public void sendBlockchainToPeer(String peerUrl, Blockchain blockchain) {
        try {
            String blockchainJson = objectMapper.writeValueAsString(blockchain);
            restTemplate.postForObject(peerUrl + "/blockchain", blockchainJson, String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}