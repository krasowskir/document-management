package org.richard.home.web;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;

import static org.hamcrest.Matchers.matchesPattern;

class DocumentServletIT {


    @Test
    void testDummyServlet() {
        RestAssured
                .get("/api/player?name=richard")
                .then()
                .statusCode(200)
                .and()
                .body(
                        matchesPattern(".*richard$"));
    }

    @Test
    void testUpload() throws URISyntaxException {


        RestAssured.
                given()
                .multiPart(new File(this.getClass().getClassLoader().getResource("files/myFile.txt").toURI()))
                .post("/api/documents")
                .then()
                .statusCode(200);

    }
}