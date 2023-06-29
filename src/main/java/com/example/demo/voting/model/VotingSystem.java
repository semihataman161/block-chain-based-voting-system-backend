package com.example.demo.voting.model;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class VotingSystem {
    private Blockchain blockchain;
    private Map<String, Integer> candidateVotes;
    private List<String> candidates;

    public VotingSystem() {
        candidateVotes = new HashMap<>();
        candidates = Arrays.asList("Candidate1", "Candidate2", "Candidate3", "Candidate4");
    }

    public void setBlockchain(Blockchain blockchain) {
        this.blockchain = blockchain;
        synchronizeCandidateVotes();
    }

    public synchronized void castVote(String voterId, String candidate, PrivateKey privateKey, PublicKey publicKey) {
        if (hasVoted(voterId)) {
            System.out.println("You have already cast your vote.");
            return;
        }

        if (!isValidCandidate(candidate)) {
            System.out.println("Invalid candidate.");
            return;
        }

        Transaction transaction = new Transaction(voterId, candidate);
        transaction.generateSignature(privateKey);

        if (!transaction.verifySignature(publicKey)) {
            System.out.println("Invalid transaction signature.");
            return;
        }

        Block newBlock = new Block(blockchain.getLatestBlock().getIndex() + 1,
                blockchain.getLatestBlock().getHash(), transaction);

        if (ConsensusProtocol.reachConsensus(blockchain)) {
            blockchain.addBlock(newBlock);
            updateCandidateVotes(candidate);
            System.out.println("Casted vote successfully!");
        } else {
            System.out.println("Consensus not reached. Vote not cast.");
        }
    }

    private void updateCandidateVotes(String candidate) {
        candidateVotes.put(candidate, candidateVotes.getOrDefault(candidate, 0) + 1);
    }

    private void synchronizeCandidateVotes() {
        candidateVotes.clear();
        for (Block block : blockchain.getChain()) {
            Transaction transaction = block.getData();
            if (transaction != null && !transaction.getCandidate().equals("Genesis Block")) {
                String candidate = transaction.getCandidate();
                candidateVotes.put(candidate, candidateVotes.getOrDefault(candidate, 0) + 1);
            }
        }
    }

    public boolean hasVoted(String voterId) {
        for (Block block : blockchain.getChain()) {
            Transaction transaction = block.getData();
            if (transaction != null && transaction.getVoterId().equals(voterId)) {
                return true;
            }
        }
        return false;
    }

    private boolean verifyHash(String voter, String hashedData) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(voter.getBytes(StandardCharsets.UTF_8));
            String hashedVoter = bytesToHex(hashBytes);
            return hashedVoter.equals(hashedData);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public boolean isValidCandidate(String candidate) {
        return candidates.contains(candidate);
    }

    public List<Block> getChain() {
        return blockchain.getChain();
    }

    public Map<String, Integer> calculateResults() {
        return candidateVotes;
    }

    public List<String> getCandidates() {
        return candidates;
    }
}