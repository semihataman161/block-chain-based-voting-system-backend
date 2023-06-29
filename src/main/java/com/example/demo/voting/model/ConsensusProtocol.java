package com.example.demo.voting.model;

public class ConsensusProtocol {
    public static boolean reachConsensus(Blockchain blockchain) {
        Block latestBlock = blockchain.getLatestBlock();
        Block previousBlock = blockchain.getPreviousBlock(latestBlock);

        if (previousBlock == null) {
            // The blockchain is empty, so the latest block is considered valid
            return true;
        }

        return previousBlock.getHash().equals(latestBlock.getPreviousHash());
    }

    public static boolean validateBlock(Block block) {
        String hash = block.calculateHash();
        return hash.equals(block.getHash());
    }
}

