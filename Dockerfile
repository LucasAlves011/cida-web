# Imagem base com Amazon Corretto 24 (versão Alpine para tamanho reduzido)
FROM amazoncorretto:24-alpine-jdk

WORKDIR /app

COPY target/*.jar app.jar

# Configurações de segurança - usuário não-root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser:appgroup

# Porta que a aplicação expõe (ajuste conforme necessário)
EXPOSE 8080

# Comando de execução (pode adicionar parâmetros JVM aqui)
ENTRYPOINT ["java", "-jar", "app.jar"]