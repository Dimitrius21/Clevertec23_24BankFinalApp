#Build stage

FROM gradle:latest AS BUILD
WORKDIR /app
COPY . .
RUN gradle clean build

# Package stage

FROM eclipse-temurin:17-alpine
ENV JAR_NAME=app-1.0.jar
ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME .
EXPOSE 8888
ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME

