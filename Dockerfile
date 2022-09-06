FROM adoptopenjdk/openjdk11:alpine-jre

LABEL maintener="Asep Ridwanullah <asep.ridwan@gmail.com>"

ENV APPLICATION_PORT=8880
ENV PROFILE=default
ENV DATABASE_USER=docker
ENV DATABASE_PASSWORD=docker
ENV DATABASE_HOST=192.168.1.19
ENV DATABASE_NAME=kedukasi
ENV DATABASE_PORT=5432

VOLUME /tmp
ADD config ./
RUN mkdir -p ./uploads
COPY target/kedukasi-service-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
