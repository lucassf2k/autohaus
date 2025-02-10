# AUTOHAUS

## Description

Implementation of a car store system using RMI (Remote Method Invocation) for communication between different servers, developed for the Distributed Systems discipline.

## Implementation

The system is designed by dividing all functionalities into smaller services, each corresponding to a complete feature.

### System Components

- **Client:** A terminal-based frontend responsible for reading and displaying data.
- **Crypto:** Contains all algorithms used by the security service.
- **Authentication:** Handles the sign-in logic of the system.
- **Database:** An in-memory database service implementing necessary abstractions for application operations.
- **Firewall:** Implements a reverse proxy based on service ports, featuring a blocking system for unauthorized ports.
- **Sdc:** A service responsible for encrypting all messages exchanged between services.
  - Encryption methods used: HMAC, Vernam cipher, and RSA.
- **Gateway:** Acts as an intermediary between all services.
- **Agnix:** A basic implementation of Nginx, featuring the following load-balancing algorithms:
  - Least Connections
  - Round Robin
  - Weighted Round Robin

## Tools

- RMI
- Java 17
- Vernam Cipher
- RSA

## License

This project is licensed under the MIT License. See the LICENSE file in the repository for more details.
