# Estágio de build - usa imagem com Maven/Gradle e JDK
FROM amazoncorretto:23-alpine as builder

WORKDIR /app

# Copia os arquivos do projeto
COPY pom.xml .
COPY src ./src

# Instala o Maven (para projetos Maven)
RUN apk add --no-cache maven && \
    mvn clean package

# Estágio final - usa imagem mínima apenas com JRE
FROM amazoncorretto:23-alpine-jre

WORKDIR /app

# Copia o JAR do estágio de build
COPY --from=builder /app/target/sua-aplicacao.jar app.jar

# Expõe a porta
EXPOSE 8080

# Define variáveis de ambiente
ENV JAVA_OPTS=""
ENV APP_ENV="production"

# Comando de execução
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]