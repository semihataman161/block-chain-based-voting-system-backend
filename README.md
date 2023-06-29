# CONSENSUS ALGORITHM
In our solution, we employ a Proof of Authority (PoA) consensus algorithm, an apt choice for a private blockchain environment. It maintains high efficiency and reduces the chances of attacks, making it suitable for a system where all participants can be trusted. Each vote is validated before it's added to the blockchain, and each vote is tied to a unique pair of RSA keys, ensuring the authenticity and non-repudiation of each vote.
<br>
# PROTOTYPE DESIGN & IMPLEMENTATION
The proposed solution was implemented using Java Spring Boot. We provide a brief explanation of the key classes used in our implementation.
<br>
• Voting Service:
The backbone of our system facilitates the interaction between different components. It handles operations related to voting, including casting a vote, calculating results, and synchronizing the blockchain across peers. The system uses the @Scheduled annotation to regularly trigger synchronization, ensuring consistency across all peers.
<br>
• Blockchain Controller:
It exposes the blockchain to the outside world via a REST API. This class serves the current state of the blockchain to any requesting entity.
<br>
• Voting Controller:
Like the Blockchain Controller, this class exposes voting functionality via a REST API. It includes methods to cast a vote and to retrieve voting results.
<br>
• Web Configuration:
As part of our security measures, this class handles Cross-Origin Resource Sharing (CORS) to prevent cross-origin attacks. This makes our application accessible from any origin while maintaining its security.
<br>
• Voting Configuration:
This class is responsible for the setup of our voting system. It initializes our Voting System object and ties it to the blockchain served by our peer service.
<br>
• Startup Configuration:
This class triggers the casting of votes when the application starts, mimicking real-world usage and demonstrating the functionality of our system.
<br>
• Voting Service:
This service provides the core voting functionalities such as casting votes and getting voting results. It interacts with other services such as VotingSystem, PeerService, and PeerNetworkingService. It is annotated with @Service and @Component, meaning it is a Spring-managed bean and is automatically instantiated at runtime. The castVote function is used to vote for a candidate using the provided voter ID, candidate name, and keys. The getVotingResults function returns the current voting results. The synchronizeBlockchain function, scheduled to run every 15 seconds, is responsible for synchronizing the current node's blockchain with its peers.
<br>
• Peer Service:
This class manages the local copy of the blockchain. It has functions to get and set the current blockchain.
<br>
• Peer Networking Service:
This service manages communication between different nodes in the network. It has functions to asynchronously fetch the blockchain from a peer node and send the local copy of the blockchain to a peer node.
<br>
• Block
This class represents a block in the blockchain. Each block contains an index, timestamp, hash of the previous block, its own hash, and a transaction (the vote). Blocks are validated by comparing their stored hash with a newly calculated hash.
<br>
• String Util
This class contains utility methods for hashing a string using SHA-256.
<br>
• Blockchain
This class represents the entire blockchain, which is a list of blocks. After validating it using the consensus protocol, the addBlock function adds a block to the chain. The isValid function validates the entire blockchain by checking the hashes of all blocks and their sequence.
<br>
• Consensus Protocol
This class contains static methods that are used to validate blocks and ensure that all nodes in the network agree on the state of the blockchain.
<br>
• Transaction
This class represents a vote transaction. Each transaction contains a voter ID, the name of the candidate, a transaction ID, and a digital signature. The generateSignature function creates a digital signature for the transaction. The verifySignature function verifies a digital signature.
<br>
• Voting System
This class manages the voting system. It maintains a list of candidates and a map of votes for each candidate. The castVote function is used to vote for a candidate. It creates a new transaction and a new block, then adds the block to the blockchain. The calculateResults function calculates the current voting results by counting the votes for each candidate.
<br>
Each class is designed with specific responsibilities, ensuring the separation of concerns in the system. The system's architecture is modular and follows the single responsibility principle, making it easy to maintain and extend.
<br>
The private nature of our blockchain network provides an optimal balance of transparency and security, as only trusted nodes can participate in the network. We also optimized for performance and scalability by using thread pools for simultaneous voting and blockchain operations.
<br>
# PERFORMANCE
Our prototype demonstrates robust, efficient, and scalable performance. It can handle multiple simultaneous votes, updating the blockchain in real-time while maintaining the overall system's integrity and security. Furthermore, the blockchain synchronization mechanism ensures that all peers have the most recent and accurate version of the blockchain, crucial for the reliability and credibility of voting results.
