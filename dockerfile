##Compile image
FROM gradle:7.5.1-jdk11-alpine AS build
WORKDIR /workspace/app

COPY build.gradle.kts settings.gradle.kts ./

RUN gradle clean build --no-daemon > /dev/null 2>&1 || true

COPY src src

RUN gradle clean build --no-daemon


#Binary image
FROM openjdk:11-jre-slim
VOLUME /tmp

ARG DEPENDENCY=/workspace/app/build/libs

ENV PORT=8080
ENV RABBITMQ_HOST=192.168.0.2

COPY --from=build ${DEPENDENCY} /app/lib
ENTRYPOINT ["java","-jar","/app/lib/app.jar"]