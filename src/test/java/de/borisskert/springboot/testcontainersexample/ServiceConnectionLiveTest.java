package de.borisskert.springboot.testcontainersexample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasItems;

import java.util.List;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
// Testcontainers require a valid docker installation.
// When running the tests, ensure you have a valid Docker environment
class ServiceConnectionLiveTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @Autowired
    private MiddleEarthCharactersRepository repository;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
    }

    @Test
    void whenRequestingHobbits_thenReturnFrodoAndSam() {
        repository.saveAll(List.of(
                new MiddleEarthCharacter("Frodo", "hobbit"),
                new MiddleEarthCharacter("Samwise", "hobbit"),
                new MiddleEarthCharacter("Aragon", "human"),
                new MiddleEarthCharacter("Gandalf", "wizard")
        ));

        when().get("/characters?race=hobbit")
                .then().statusCode(200)
                .and().body("name", hasItems("Frodo", "Samwise"));
    }
}
