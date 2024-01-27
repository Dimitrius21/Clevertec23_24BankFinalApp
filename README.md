## BanKFinalApp веб-сервис реализующий функционал для работы с банковскими сущностями клиента.

***

### Технологии, которые использовали на проекте:

* Java 17
* Gradle 8.4
* Lombok plugin 8.4
* Postgresql
* Spring-boot 3.1.7
* Spring-boot-starter-data-jpa
* Spring-boot-starter-data-redis
* Spring-boot-starter-web
* Spring-boot-starter-validation
* Spring-boot-starter-aop
* Spring-boot-starter-amqp
* Spring-boot-configuration-processor
* Spring-boot-starter-security
* Spring-boot-starter-oauth2-resource-server
* Spring-cloud-starter-openfeign
* Spring-cloud-config-server
* Spring-cloud-starter-config
* Spring-cloud-starter-netflix-eureka-server
* Spring-cloud-starter-netflix-eureka-client
* Spring-cloud-starter-gateway
* Springdoc-openapi-starter-webmvc-ui
* Springdoc-openapi-starter-webflux-ui
* Jsonwebtoken-jjwt-api
* Jsonwebtoken-jjwt-impl
* Jsonwebtoken-jjwt-jackson
* Liquibase
* Mapstruct
* Spring-boot-starter-test
* Spring-security-test
* Wiremock-standalone
* Testcontainers-Postgresql

***

### Инструкция для запуска приложения в докере:

* Запустить в корне проекта `docker-compose up`.

***

### Запуск всех тестов локально(покрытие 85% строк):

* Запустить в корне проекта `./gradlew test`

***

### Документация Open-api(Swagger)

1. Чтобы посмотреть Swagger документацию, нужно перейти по ссылке: `http://localhost:8765/webjars/swagger-ui/index.html`
2. Это общая документация работающая через api-gateway, справа вверху будет выпадающий список для переключения между
   api.
3. Некоторые конечные точки будут закрыты и для них нужен jwt, получить его можно выбрав в выпадающем
   списке `Customer Service`. И использовать post метод для генерации jwt.
4. Так же документации доступны и напрямую из сервисов, они находятся по url:
    * Customer Service - `http://localhost:8082/customers/swagger-ui/index.html`
    * Product Service - `http://localhost:8081/products/swagger-ui/index.html`
    * Rate Service - `http://localhost:8083/rate/swagger-ui/index.html`

***
