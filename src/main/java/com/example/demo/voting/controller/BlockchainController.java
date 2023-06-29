package com.example.demo.voting.controller;

import com.example.demo.voting.model.Blockchain;
import com.example.demo.voting.service.PeerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlockchainController {

    private final PeerService peerService;

    public BlockchainController(PeerService peerService) {
        this.peerService = peerService;
    }

    @GetMapping("/blockchain")
    public Blockchain getBlockchain() {
        return peerService.getBlockchain();
    }
}

