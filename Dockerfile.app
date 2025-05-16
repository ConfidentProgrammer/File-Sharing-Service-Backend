FROM openjdk:21-jdk-slim

RUN useradd -m -s /bin/bash appuser
USER appuser

WORKDIR /fileshare
RUN mkdir -p /fileshare/uploads && chown -R appuser:appuser /fileshare/uploads

COPY target/file-sharing-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]