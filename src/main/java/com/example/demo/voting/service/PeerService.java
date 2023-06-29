package com.example.demo.voting.service;

import com.example.demo.voting.model.Blockchain;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class PeerService {
    private Blockchain blockchain = new Blockchain();

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(Blockchain blockchain) {
        this.blockchain = blockchain;
    }
}
