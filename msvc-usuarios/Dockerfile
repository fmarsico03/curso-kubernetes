ARG MSVC_NAME=msvc-usuarios

FROM eclipse-temurin:17-jdk-alpine AS builder
ARG MSVC_NAME
WORKDIR /app/$MSVC_NAME

COPY ./pom.xml /app
COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw .
COPY ./$MSVC_NAME/pom.xml .
RUN chmod +x mvnw

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./$MSVC_NAME/src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
ARG MSVC_NAME

WORKDIR /app
RUN mkdir ./logs
ARG TARGET_FOLDER=/app/$MSVC_NAME/target
COPY --from=builder $TARGET_FOLDER/msvc-usuarios-0.0.1-SNAPSHOT.jar .
ENV PORT=8081
ENV DB_HOST=mysql8-usuarios:3306
ENV DB_DATABASE=msvc_usuarios
ENV DB_USERNAME=root
ENV DB_PASSWORD=root
ENV CURSOS_URL=msvc-cursos:8082
EXPOSE $PORT

ENTRYPOINT ["java", "-jar","msvc-usuarios-0.0.1-SNAPSHOT.jar"]