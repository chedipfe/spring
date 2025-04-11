FROM openjdk:17-jdk-slim AS builder
WORKDIR /app
COPY .mvn/ ./.mvn
COPY mvnw pom.xml  ./
RUN sed -i 's/\r$//' mvnw \
&& ./mvnw dependency:go-offline
COPY . .
RUN sed -i 's/\r$//' mvnw \
&& ./mvnw package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
RUN useradd -ms /bin/bash myuser
USER myuser
COPY --from=builder --chown=myuser:myuser /app/target/*.jar run.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/run.jar"]
