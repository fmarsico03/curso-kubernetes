ARG MSVC_NAME=msvc-gateway

FROM eclipse-temurin:17-jdk-alpine AS builder
ARG MSVC_NAME
WORKDIR /app/$MSVC_NAME

COPY ./pom.xml /app
COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw .
COPY ./$MSVC_NAME/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./$MSVC_NAME/src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
ARG MSVC_NAME

WORKDIR /app
RUN mkdir ./logs
ARG TARGET_FOLDER=/app/$MSVC_NAME/target
COPY --from=builder $TARGET_FOLDER/msvc-gateway-0.0.1-SNAPSHOT.jar .
ENV PORT=8080
ENV CURSOS_URL=msvc-cursos:8082
ENV USUARIOS_URL=msvc-usuarios:8081
EXPOSE $PORT

ENTRYPOINT ["java", "-jar","msvc-gateway-0.0.1-SNAPSHOT.jar"]