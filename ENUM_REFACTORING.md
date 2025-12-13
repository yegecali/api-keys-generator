# Refactorización con Enum CryptoAlgorithm

## Cambios Realizados

### 1. Creación del Enum `CryptoAlgorithm`
Se creó una clase Enum centralizada para manejar los identificadores de algoritmos de encriptación:

```java
public enum CryptoAlgorithm {
    RSA("RSA"),
    AES_GCM("AES_GCM");
    
    public String getValue() { ... }
    public static CryptoAlgorithm fromString(String value) { ... }
}
```

### 2. Actualización de Clases DTO
- **`CryptoRequest`**: Campo `type` cambió de `String` a `CryptoAlgorithm`
- **`EncryptRsaRequest`**: Ahora inicializa `type` con `CryptoAlgorithm.RSA`
- **`EncryptAesRequest`**: Ahora inicializa `type` con `CryptoAlgorithm.AES_GCM`

### 3. Actualización de Strategies
- **`RsaCryptoStrategy`**: 
  - `getAlgorithm()` retorna `CryptoAlgorithm.RSA.getValue()`
  - Importa `CryptoAlgorithm`

- **`AesGcmCryptoStrategy`**: 
  - `getAlgorithm()` retorna `CryptoAlgorithm.AES_GCM.getValue()`
  - Importa `CryptoAlgorithm`

- **`RsaKeyGenerationStrategy`**: 
  - `getType()` retorna `CryptoAlgorithm.RSA.getValue()`
  - `model.setType()` usa `CryptoAlgorithm.RSA.getValue()`

- **`AesKeyGenerationStrategy`**: 
  - `getType()` retorna `CryptoAlgorithm.AES_GCM.getValue()`
  - `model.setType()` usa `CryptoAlgorithm.AES_GCM.getValue()`

### 4. Actualización de Validators
- **`CryptoRequestValidator`**: 
  - Importa `CryptoAlgorithm`
  - Usa `switch` con `CryptoAlgorithm` en lugar de strings

### 5. Actualización de Factories
- **`KeyGeneratorFactory`**: 
  - Inicializa `type` con `CryptoAlgorithm.RSA.getValue()` si es null
  - Importa `CryptoAlgorithm`

- **`CryptoStrategyFactory`**: 
  - Nuevo método `get(CryptoAlgorithm algorithm)` para aceptar enum directamente
  - Mantiene método `get(String type)` para backward compatibility
  - Importa `CryptoAlgorithm`

## Ventajas de esta Implementación

✅ **Type-Safe**: El compilador verifica los algoritmos válidos
✅ **Centralizado**: Un único punto de verdad para los nombres de algoritmos
✅ **Fácil de mantener**: Agregar nuevos algoritmos es simple
✅ **Backward Compatible**: El JSON sigue funcionando normalmente
✅ **Legible**: El código es más claro y autodocumentado
✅ **Menos propenso a errores**: No hay strings mágicos dispersos en el código

## Ejemplo de uso

### Genera claves RSA
```bash
GET /keys?type=RSA&size=2048
```

### Genera claves AES-GCM
```bash
GET /keys?type=AES_GCM&size=256
```

### Encripta con RSA
```json
POST /crypto/encrypt
{
  "type": "RSA",
  "key": "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA...",
  "payload": "Mi mensaje secreto"
}
```

### Encripta con AES-GCM
```json
POST /crypto/encrypt
{
  "type": "AES_GCM",
  "key": "dGVzdGtleWZvcnRlc3RpbmdwdXJwb3Nlc2lzMzI=",
  "payload": "Mi mensaje secreto"
}
```

## Nota sobre Jackson y JSON

Jackson automáticamente serializa y deserializa el enum `CryptoAlgorithm` usando su nombre (RSA, AES_GCM) en el JSON, por lo que el cliente sigue enviando strings normales como antes, pero internamente el código usa enums type-safe.

