package com.example.obrestdatajpa.controller;

import com.example.obrestdatajpa.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {


    private TestRestTemplate testRestTemplate;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    //Capturamos el puerto por el que se ejecuta el test
    @LocalServerPort
    private int port;

    private final String ROOT_API= "http://localhost:";


    @BeforeEach
    void setUp() {
    restTemplateBuilder = restTemplateBuilder.rootUri(ROOT_API + port);
    this.testRestTemplate = new TestRestTemplate(restTemplateBuilder);
    }

    @Test
    void listAll() {
        ResponseEntity<Book[]> response =
                testRestTemplate.getForEntity("/api/books", Book[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void showBook() {
        ResponseEntity<Book> response =
                testRestTemplate.getForEntity("/api/books/1", Book.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void createBook() {

        /*
        Preparamos las cabeceras de la peticion
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        /*
        Creamos el Json de prueba
         */
        String jsonBook = """
                {
                    "title": "Star War 2 desde el Test",
                    "author": "Silany Marly",
                    "pages": 536,
                    "price": 193.45,
                    "releaseDate": "1994-12-25",
                    "online": true
                }
                          """;

         /*
        Creamos la peticion con el body, cabeceras
         */
        HttpEntity request = new HttpEntity(jsonBook,headers);

         /*
        Capturamos la respuesta
         */
        ResponseEntity<Book> response =
                testRestTemplate.exchange("/api/books",HttpMethod.POST,request,Book.class);

         /*
        Evaluamos
         */
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}