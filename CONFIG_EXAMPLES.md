# Ejemplos de Configuración por Entorno

Esta carpeta contiene ejemplos de cómo configurar la aplicación para diferentes entornos.

## Desarrollo (development)

**archivo:** `application-dev.yaml`

```yaml
quarkus:
  log:
    level: DEBUG
    category:
      "com.yegecali.keysgenerator":
        level: TRACE

app:
  key-generation:
    rsa:
      allowed-sizes: [2048, 3072, 4096]
      default-size: 2048
    aes:
      allowed-sizes: [128, 192, 256]
      default-size: 256
```

**Activar en desarrollo:**
```bash
export QUARKUS_PROFILE=dev
mvn quarkus:dev
```

---

## Testing (test)

**archivo:** `application-test.yaml`

```yaml
quarkus:
  log:
    level: INFO
  http:
    port: 8081

app:
  key-generation:
    rsa:
      allowed-sizes: [2048]        # Solo tamaño mínimo en tests
      default-size: 2048
    aes:
      allowed-sizes: [256]          # Solo tamaño único en tests
      default-size: 256
```

**Activar en testing:**
```bash
export QUARKUS_PROFILE=test
mvn test
```

---

## Producción (production)

**archivo:** `application-prod.yaml`

```yaml
quarkus:
  log:
    level: WARN
  http:
    port: 8443
    ssl:
      certificate:
        key-store-file: /etc/secrets/keystore.p12
        key-store-password: ${KEYSTORE_PASSWORD}

app:
  key-generation:
    rsa:
      allowed-sizes: [4096]         # Solo tamaños seguros
      default-size: 4096
    aes:
      allowed-sizes: [256]          # Solo AES-256
      default-size: 256
  
  crypto:
    rsa:
      algorithm: "RSA/ECB/OAEPWithSHA-512AndMGF1Padding"  # Más seguro
    aes-gcm:
      tag-length: 128
      iv-size: 12
```

**Activar en producción:**
```bash
export QUARKUS_PROFILE=prod
export KEYSTORE_PASSWORD=mi_contraseña_segura
java -jar target/quarkus-app/quarkus-run.jar
```

---

## Variables de Entorno Comunes

### Sobrescribir mediante variables (sin perfiles)

```bash
# Cambiar puerto
export QUARKUS_HTTP_PORT=9000

# Cambiar nivel de log
export QUARKUS_LOG_LEVEL=DEBUG

# Cambiar configuración de claves
export APP_KEY_GENERATION_RSA_DEFAULT_SIZE=4096
export APP_KEY_GENERATION_AES_DEFAULT_SIZE=256

# Cambiar algoritmos
export APP_CRYPTO_RSA_ALGORITHM="RSA/ECB/OAEPWithSHA-512AndMGF1Padding"
export APP_CRYPTO_AES_GCM_TAG_LENGTH=128

# Log específico
export QUARKUS_LOG_CATEGORY__COM_YEGECALI_KEYSGENERATOR__LEVEL=TRACE
```

---

## Docker Compose Example

```yaml
version: '3.8'

services:
  keys-generator:
    image: keys-generator:latest
    ports:
      - "8080:8080"
    environment:
      QUARKUS_PROFILE: prod
      QUARKUS_HTTP_PORT: 8080
      APP_KEY_GENERATION_RSA_DEFAULT_SIZE: 4096
      APP_KEY_GENERATION_AES_DEFAULT_SIZE: 256
      APP_CRYPTO_RSA_ALGORITHM: "RSA/ECB/OAEPWithSHA-512AndMGF1Padding"
      QUARKUS_LOG_LEVEL: WARN
    volumes:
      - ./config/application-prod.yaml:/config/application-prod.yaml
```

---

## Kubernetes ConfigMap Example

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: keys-generator-config
data:
  application-prod.yaml: |
    quarkus:
      log:
        level: INFO
      http:
        port: 8080
    app:
      key-generation:
        rsa:
          allowed-sizes: [4096]
          default-size: 4096
        aes:
          allowed-sizes: [256]
          default-size: 256
      crypto:
        rsa:
          algorithm: "RSA/ECB/OAEPWithSHA-512AndMGF1Padding"

---
apiVersion: v1
kind: Pod
metadata:
  name: keys-generator
spec:
  containers:
  - name: api
    image: keys-generator:latest
    env:
    - name: QUARKUS_PROFILE
      value: "prod"
    volumeMounts:
    - name: config
      mountPath: /config
  volumes:
  - name: config
    configMap:
      name: keys-generator-config
```

---

## Resumen de Estrategias

| Estrategia | Caso de Uso | Complejidad |
|-----------|-----------|-----------|
| Perfiles YAML | Diferentes valores por entorno | Baja |
| Variables de Entorno | Valores secretos o dinámicos | Muy Baja |
| ConfigMap (K8s) | Orquestación con Kubernetes | Media |
| Docker Compose | Desarrollo local containerizado | Baja |
| CLI Properties | Pruebas ad-hoc | Muy Baja |

---

## Validar Configuración Cargada

Durante la ejecución, puedes ver las configuraciones que Quarkus carga:

```bash
# Habilitar logs detallados de SmallRye Config
export QUARKUS_LOG_CATEGORY__IO_SMALLRYE_CONFIG__LEVEL=DEBUG

mvn quarkus:dev
```

Verás en los logs:
```
[DEBUG] Loading configuration from: file:///app/config/application.yaml
[DEBUG] Loading profile: prod
[DEBUG] Configuration sources: [PropertiesConfigSource, YamlConfigSource, EnvConfigSource]
```

