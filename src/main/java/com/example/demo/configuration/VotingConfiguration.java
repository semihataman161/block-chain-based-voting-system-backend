package com.example.demo.configuration;

import com.example.demo.voting.model.VotingSystem;
import com.example.demo.voting.service.PeerService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class VotingConfiguration {


    private final PeerService peerService;

    @Bean
    public VotingSystem votingSystem() {
        VotingSystem votingSystem = new VotingSystem();
        votingSystem.setBlockchain(peerService.getBlockchain());
        return votingSystem;
    }
}
