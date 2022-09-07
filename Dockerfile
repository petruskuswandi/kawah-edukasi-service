FROM adoptopenjdk/openjdk11:alpine-jre
RUN apk add --no-cache tzdata &&\
    cp /usr/share/zoneinfo/Asia/Jakarta /etc/localtime &&\
    echo "Asia/Jakarta" >  /etc/timezone &&\
    date
LABEL maintener="Asep Ridwanullah <asep.ridwan@gmail.com>"

ENV APPLICATION_PORT=8880
ENV PROFILE=default
ENV DATABASE_USER=postgres
ENV DATABASE_PASSWORD=Password09!
ENV DATABASE_HOST=postgres
ENV DATABASE_NAME=kawah_db
ENV DATABASE_PORT=5432

VOLUME /tmp
ADD config ./
RUN mkdir -p ./uploads

COPY target/kedukasi-service-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
