package com.example.demo.voting.service;

import com.example.demo.voting.model.Blockchain;
import com.example.demo.voting.model.VotingSystem;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Component
@AllArgsConstructor
public class VotingService {
    private VotingSystem votingSystem;
    private PeerService peerService;
    private PeerNetworkingService peerNetworkingService;

    public void castVote(String voterId, String candidate, PrivateKey privateKey, PublicKey publicKey) {
        votingSystem.castVote(voterId, candidate, privateKey, publicKey);
    }

    public Map<String, Integer> getVotingResults() {
        return votingSystem.calculateResults();
    }

    public List<String> getCandidates() {
        return votingSystem.getCandidates();
    }

    @Scheduled(fixedDelay = 15000) // 15 seconds
    public void synchronizeBlockchain() {
        // List of peers could be fetched from a config file or other source
        List<String> peerUrls = Arrays.asList("http://localhost:8081", "http://localhost:8082",
                "http://localhost:8083", "http://localhost:8084", "http://localhost:8085",
                "http://localhost:8086", "http://localhost:8087");

        ExecutorService executorService = Executors.newFixedThreadPool(peerUrls.size());

        for (String peerUrl : peerUrls) {
            executorService.submit(() -> {
                try {
                    System.out.println("SYNCING WITH PEER AT " + peerUrl);
                    CompletableFuture<Blockchain> blockchainFuture = peerNetworkingService.getBlockchainFromPeerAsync(peerUrl);
                    Blockchain peerBlockchain = blockchainFuture.get(); // Wait for the result of the CompletableFuture

                    if (peerBlockchain != null && peerBlockchain.isValid() && peerBlockchain.getChain().size() > votingSystem.getChain().size()) {
                        peerService.setBlockchain(peerBlockchain);
                        votingSystem.setBlockchain(peerBlockchain);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Error synchronizing with peer at " + peerUrl + ": " + e.getMessage());
                }
            });
        }

        executorService.shutdown();
    }
}

