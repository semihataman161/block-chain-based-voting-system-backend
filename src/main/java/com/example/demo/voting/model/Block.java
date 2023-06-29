package com.example.demo.voting.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@NoArgsConstructor
public class Block implements Serializable {
    private int index;
    private long timestamp;
    private String previousHash;
    private String hash;
    private Transaction data;

    private boolean valid;

    public Block(int index, String previousHash, Transaction data) {
        this.index = index;
        this.previousHash = previousHash;
        this.data = data;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public int getIndex() {
        return index;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public Transaction getData() {
        return data;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }

    public String calculateHash() {
        // A simple implementation of calculating the hash using SHA-256
        // modify the hash calculation to use Transaction's transactionId instead of data
        String calculatedHash = StringUtil.applySha256(
                previousHash + Long.toString(timestamp) + Integer.toString(index) + data.getTransactionId());
        return calculatedHash;
    }

    public boolean isValid() {
        // Check if the current hash is valid by comparing it with the calculated hash
        return hash.equals(calculateHash());
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}

class StringUtil {
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

