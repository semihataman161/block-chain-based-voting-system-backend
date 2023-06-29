package com.example.demo.voting.model;

import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Base64;
@NoArgsConstructor
public class Transaction {
    private String voterId;
    private String candidate;
    private String transactionId;
    private byte[] signature;

    public Transaction(String voterId, String candidate) {
        this.voterId = voterId;
        this.candidate = candidate;
        this.transactionId = calculateTransactionId();
    }

    private String calculateTransactionId() {
        String data = voterId + candidate;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = voterId + candidate + transactionId;
        try {
            Signature signatureAlgorithm = Signature.getInstance("SHA256withRSA");
            signatureAlgorithm.initSign(privateKey);
            signatureAlgorithm.update(data.getBytes());
            this.signature = signatureAlgorithm.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
    }

    public boolean verifySignature(PublicKey publicKey) {
        String data = voterId + candidate + transactionId;
        try {
            Signature signatureAlgorithm = Signature.getInstance("SHA256withRSA");
            signatureAlgorithm.initVerify(publicKey);
            signatureAlgorithm.update(data.getBytes());
            return signatureAlgorithm.verify(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getVoterId() {
        return voterId;
    }

    public String getCandidate() {
        return candidate;
    }

    public String getTransactionId() {
        return transactionId;
    }
}

