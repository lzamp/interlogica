# Utilizza un'immagine di base con Java installato
FROM openjdk:11-jre-slim


# Imposta la directory di lavoro all'interno dell'immagine Docker
WORKDIR /app

# Copia il file .jar nella directory dell'immagine Docker
COPY phoneValidator-1.0-SNAPSHOT.jar /app/phoneValidator.jar

# Esponi la porta su cui il server Spring Boot sarà in ascolto (assicurati che corrisponda alle configurazioni del tuo progetto Spring Boot)
EXPOSE 8080

# Comando per avviare il server Spring Boot all'interno dell'immagine Docker
CMD ["java", "-jar", "phoneValidator.jar"]
