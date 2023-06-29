package com.example.demo.voting.model;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> chain;
    private boolean valid;
    private Block latestBlock;

    public Blockchain() {
        chain = new ArrayList<>();
        // Create the genesis block
        Block genesisBlock = createGenesisBlock();
        chain.add(genesisBlock);
    }

    private Block createGenesisBlock() {
        // Create the first block in the blockchain (genesis block)
        Transaction genesisTransaction = new Transaction("0", "Genesis Block");
        return new Block(0, "0", genesisTransaction);
    }

    public void addBlock(Block block) {
        if (ConsensusProtocol.validateBlock(block)) {
            chain.add(block);
        } else {
            System.out.println("Invalid block. Consensus not reached.");
        }
    }
    public Block getPreviousBlock(Block block) {
        int index = block.getIndex();
        if (index > 0 && index <= chain.size()) {
            return chain.get(index - 1);
        }
        return null;
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public boolean isValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            // Check if the current block's hash is valid
            if (!currentBlock.isValid()) {
                return false;
            }

            // Check if the previous hash matches the hash of the previous block
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<Block> getChain() {
        return chain;
    }
    public void setLatestBlock(Block latestBlock) {
        this.latestBlock = latestBlock;
    }
}
