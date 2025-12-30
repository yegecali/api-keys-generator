# 🔐 API Keys Generator

Una API REST para generar llaves criptográficas y realizar operaciones de cifrado/descifrado con **RSA** y **AES-GCM**.

Este README resume cómo ejecutar y desarrollar el proyecto, además de documentar los cambios y decisiones recientes.

---

## Requisitos
- Maven 3.8+ (usado para build y generación de fuentes)
- JDK disponible en el entorno de build: la máquina tiene JDK 21 instalado, pero por compatibilidad con procesamiento de anotaciones actualmente el POM está configurado para compilar con `--release 17` (maven.compiler.release=17). Si deseas compilar con Java 21, hay que actualizar la compatibilidad de los procesadores/anotaciones (especialmente Lombok cuando se habilite).

Nota: Quarkus y el resto del código son compatibles con Java 17+ y Java 21 en tiempo de ejecución; revisa `pom.xml` si quieres restaurar `maven.compiler.release` a `21`.

---

## Cómo trabajar localmente

1) Generar las clases del contrato OpenAPI (esto también se ejecuta durante `mvn compile`):

```bash
mvn generate-sources
```

Los archivos generados se colocan en `target/generated-sources/openapi/src/main/java` y se agregan automáticamente al classpath de compilación.

2) Compilar el proyecto (usa la configuración actual del pom):

```bash
mvn -DskipTests compile
```

3) Ejecutar en modo desarrollo (live reload con Quarkus):

```bash
./mvnw quarkus:dev
# o
mvn quarkus:dev
```

4) Empaquetar la aplicación:

```bash
mvn package
```

Ejecutar el JAR:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

---

## Endpoints principales (resumen)
- GET  /keys/rsa — genera una llave RSA (por defecto 2048)
- GET  /keys?type={RSA|AES_GCM}&size={size} — genera llaves dependiendo del tipo y tamaño
- POST /crypto/encrypt — encripta (acepta `CryptoRequest`/subtipos)
- POST /crypto/decrypt — desencripta
