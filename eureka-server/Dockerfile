#Build stage

FROM gradle:latest AS BUILD
WORKDIR /eureka-server
COPY . .
RUN gradle clean build

# Package stage

FROM eclipse-temurin:17-alpine
ENV JAR_NAME=eureka-server-1.0.jar
ENV APP_HOME=/eureka-server
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME .
EXPOSE 8761
ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME

