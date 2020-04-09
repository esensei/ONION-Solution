package ru.myword.service;

import javax.annotation.PostConstruct;

import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;

/**
 * Базовый класс тестирования
 *
 * @author Губа Георгий
 * @since 31.10.2019
 */
public class BaseTest
{
    @LocalServerPort
    private int port;

    /**
     * Конфигурирование RestAssured. Указания base адреса и доп.фильтров
     */
    @PostConstruct
    public void postConstruct()
    {
        RestAssured.requestSpecification = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri("http://localhost:" + port)
            .addFilter(new ResponseLoggingFilter())//log request and response for better debugging. You can also only log if a requests fails.
            .addFilter(new RequestLoggingFilter())
            .build();
    }

}
