FROM openjdk:20-ea-11-slim-buster as build
WORKDIR /workspace/app

COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY src src

RUN ./gradlew build -x test

FROM openjdk:20-ea-11-slim-buster
VOLUME /tmp

ARG DEPENDENCY=/workspace/app/build/libs

ENV PORT=8080
ENV RABBITMQ_HOST=192.168.0.2

COPY --from=build ${DEPENDENCY} /app/lib
ENTRYPOINT ["java","-jar","/app/lib/app.jar"]
