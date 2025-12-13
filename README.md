# 🔐 API Keys Generator

A RESTful API for generating cryptographic keys and performing encryption/decryption operations using **RSA** and **AES-GCM** algorithms. Built with **Quarkus**, the Supersonic Subatomic Java Framework.

## ✨ Features

- 🔑 **Key Generation**
  - RSA key generation (2048, 3072, 4096 bits)
  - AES-GCM key generation (128, 192, 256 bits)
  
- 🔐 **Encryption/Decryption**
  - RSA encryption and decryption
  - AES-GCM encryption and decryption
  
- 🏗️ **Clean Architecture**
  - Strategy pattern for crypto operations
  - Factory pattern for key/crypto generation
  - Type-safe enums for algorithm selection
  - Input validation and error handling
  
- 🚀 **Production Ready**
  - Dependency Injection with Quarkus Arc
  - JSON serialization with Jackson
  - REST endpoints with proper HTTP status codes
  - Comprehensive error responses

## 🛠️ Prerequisites

- Java 21 or higher
- Maven 3.8.1 or higher

## 🚀 Getting Started

### Development Mode

Run the application in dev mode with live coding enabled:

```shell
./mvnw quarkus:dev
```

The Quarkus Dev UI will be available at: <http://localhost:8080/q/dev/>

### Building the Application

Package the application as a JAR:

```shell
./mvnw package
```

This produces `quarkus-run.jar` in the `target/quarkus-app/` directory with all dependencies included.

### Running the Application

```shell
java -jar target/quarkus-app/quarkus-run.jar
```

The application will start on **http://localhost:8080**

## 📡 API Endpoints

### Key Generation

#### 1. Generate RSA Keys (Default 2048 bits)
```http
GET /keys/rsa
```

**Response Example:**
```json
{
  "publicKey": "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8A...\n-----END PUBLIC KEY-----\n",
  "privateKey": "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASC...\n-----END PRIVATE KEY-----\n"
}
```

#### 2. Generate Keys (Generic Endpoint)
```http
GET /keys?type=RSA&size=4096
GET /keys?type=AES_GCM&size=256
```

**Query Parameters:**
- `type` (optional): `RSA` (default) or `AES_GCM`
- `size` (optional): Key size in bits
  - RSA: 2048, 3072, 4096 (default: 2048)
  - AES-GCM: 128, 192, 256 (default: 256)

**Response Example (AES-GCM):**
```json
{
  "key": "Base64EncodedKeyHere=="
}
```

### Encryption/Decryption

#### 3. Encrypt Data (RSA or AES-GCM)
```http
POST /crypto/encrypt
Content-Type: application/json
```

**RSA Request Body:**
```json
{
  "type": "RSA",
  "plaintext": "Hello, World!",
  "publicKey": "-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----\n"
}
```

**AES-GCM Request Body:**
```json
{
  "type": "AES_GCM",
  "plaintext": "Hello, World!",
  "key": "Base64EncodedKeyHere=="
}
```

**Response Example:**
```json
{
  "ciphertext": "Base64EncodedCiphertextHere==",
  "success": true
}
```

#### 4. Decrypt Data (RSA or AES-GCM)
```http
POST /crypto/decrypt
Content-Type: application/json
```

**RSA Request Body:**
```json
{
  "type": "RSA",
  "ciphertext": "Base64EncodedCiphertextHere==",
  "privateKey": "-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----\n"
}
```

**AES-GCM Request Body:**
```json
{
  "type": "AES_GCM",
  "ciphertext": "Base64EncodedCiphertextHere==",
  "key": "Base64EncodedKeyHere=="
}
```

**Response Example:**
```json
{
  "plaintext": "Hello, World!",
  "success": true
}
```

## 🧪 Testing the API

### Using cURL

```bash
# Generate RSA 2048 keys
curl -X GET http://localhost:8080/keys/rsa

# Generate RSA 4096 keys
curl -X GET 'http://localhost:8080/keys?type=RSA&size=4096'

# Generate AES-GCM 256 key
curl -X GET 'http://localhost:8080/keys?type=AES_GCM&size=256'

# Encrypt with RSA
curl -X POST http://localhost:8080/crypto/encrypt \
  -H 'Content-Type: application/json' \
  -d '{
    "type": "RSA",
    "plaintext": "Secret message",
    "publicKey": "-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----\n"
  }'
```

### Using Postman or Similar Tools

1. Import the endpoints from the documentation above
2. Set the appropriate content-type headers for POST requests
3. Test with different algorithm types and key sizes

## 📦 Building Native Executables

Create a native executable (requires GraalVM):

```shell
./mvnw package -Dnative
```

Or build in a container:

```shell
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

Then run the native executable:

```shell
./target/app-keys-generator-1.0.0-runner
```

## 🏗️ Project Structure

```
src/main/java/com/yegecali/keysgenerator/
├── Main.java                          # Quarkus application entry point
├── crypto/
│   └── strategies/                    # Encryption/Decryption implementations
│       ├── CryptoStrategy.java        # Interface for crypto operations
│       ├── RsaCryptoStrategy.java     # RSA implementation
│       └── AesGcmCryptoStrategy.java  # AES-GCM implementation
├── dto/                               # Data Transfer Objects
│   ├── CryptoAlgorithm.java          # Algorithm enum (RSA, AES_GCM)
│   ├── CryptoRequest.java            # Base request class
│   ├── EncryptRsaRequest.java        # RSA-specific request
│   ├── EncryptAesRequest.java        # AES-specific request
│   ├── CryptoResponse.java           # Encryption/Decryption response
│   ├── KeyResponse.java              # Key generation response
│   └── KeyGenerationRequest.java     # Key generation request
├── exception/                         # Custom exceptions
├── factory/                           # Factory pattern implementations
│   ├── KeyGeneratorFactory.java      # Creates key generators
│   └── CryptoStrategyFactory.java    # Creates crypto strategies
├── mapper/                            # Response mapping
│   └── KeyResponseMapper.java        # Maps model to response DTOs
├── model/                             # Domain models
├── resources/                         # REST endpoints
│   ├── RsaKeyResource.java           # Key generation endpoints
│   └── CryptoResource.java           # Encryption/Decryption endpoints
├── service/                           # Business logic
│   ├── KeyGenerator.java             # Key generation interface
│   └── strategies/                   # Key generation strategies
│       ├── RsaKeyGenerationStrategy.java
│       └── AesKeyGenerationStrategy.java
└── validator/                         # Input validation
    ├── KeyRequestValidator.java
    └── CryptoRequestValidator.java
```

## 🔄 Recent Refactoring

The codebase has been recently refactored to improve maintainability:

- ✅ Introduced `CryptoAlgorithm` enum for type-safe algorithm selection
- ✅ Created separate request classes (`EncryptRsaRequest`, `EncryptAesRequest`) extending `CryptoRequest`
- ✅ Improved separation of concerns with strategy and factory patterns
- ✅ Enhanced input validation and error handling
- ✅ Fixed GitHub Actions workflow to correctly use Quarkus JAR

For detailed refactoring notes, see [ENUM_REFACTORING.md](ENUM_REFACTORING.md)

## 📚 Documentation

For more information about Quarkus, visit: <https://quarkus.io/>

- [REST Guide](https://quarkus.io/guides/rest)
- [Dependency Injection Guide](https://quarkus.io/guides/cdi)
- [Maven Tooling Guide](https://quarkus.io/guides/maven-tooling)

## 📝 License

This project is open source and available under the MIT License.

## 👨‍💻 Author

**YegeCali** - Cryptographic API Generator

---

**Questions or Issues?** Please open an issue or contact the maintainers.

