# Sample Spring Boot Application

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/84bf74cf4b024afe9fc68da0b4082396)](https://app.codacy.com/gh/HalitTalha/spring-boot-exchange-api?utm_source=github.com&utm_medium=referral&utm_content=HalitTalha/spring-boot-exchange-api&utm_campaign=Badge_Grade_Settings)

#### Rate and Currency Conversion API that also stores and lists conversion request history
* Currency Data is fetched from `free.currconv.com`

## This Spring Boot application demonstrates;
* Providing Rest Services with **Spring Boot**
* Consuming Rest Services through `RestTemplate`
* Caching with scheduled eviction strategy. Spring Boot's default cache provider is employed.
* Annotation based bean validation
* Object Deserialization
* `PagingAndSortingRepository` implementation
* `ClientHttpRequestInterceptor` implementation
* Test based Automated Rest API documentation generation with **asciidoctor**
* **H2** in-memory database
* Hibernate as JPA implementation
* Mocked http-layer unit tests with `org.springframework.test.web.servlet.MockMvc`
* Repository and Service mocking with **Mockito** and **Mockito Annotations**

## REST API Documentation Link: ***/exchange-api/api-guide.html***