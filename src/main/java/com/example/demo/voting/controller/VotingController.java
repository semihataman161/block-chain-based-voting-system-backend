package com.example.demo.voting.controller;

import com.example.demo.voting.model.Block;
import com.example.demo.voting.model.Transaction;
import com.example.demo.voting.model.VotingSystem;
import com.example.demo.voting.service.VotingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vote")
public class VotingController {

    private final VotingService votingService;

    public VotingController(VotingService votingService) {
        this.votingService = votingService;
    }

    @PostMapping
    public ResponseEntity<String> castVote(@RequestParam String voterId, @RequestParam String candidate) {
        KeyPair keyPair = generateKeyPair();
        votingService.castVote(voterId, candidate, keyPair.getPrivate(), keyPair.getPublic());
        return ResponseEntity.ok("Vote has been cast successfully!");
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/results")
    public ResponseEntity<Map<String, Integer>> getVotingResults() {
        Map<String, Integer> results = votingService.getVotingResults();
        return ResponseEntity.ok(results);
    }
}

