#Build stage

FROM gradle:latest AS BUILD
WORKDIR /api-gateway
COPY . .
RUN gradle clean build

# Package stage

FROM eclipse-temurin:17-alpine
ENV JAR_NAME=api-gateway-1.0.jar
ENV APP_HOME=/api-gateway
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME .
EXPOSE 8765
ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME

