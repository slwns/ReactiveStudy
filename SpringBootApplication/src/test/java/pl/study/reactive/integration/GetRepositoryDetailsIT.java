package pl.study.reactive.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.FileCopyUtils;
import pl.study.reactive.ports.LoadRepositoryDetailsGateway;
import pl.study.reactive.storage.SpringRepositoryDetailsRepository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static java.nio.charset.StandardCharsets.UTF_8;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {WireMockInitializer.class})
public class GetRepositoryDetailsIT {

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private SpringRepositoryDetailsRepository repositoryDetailsRepository;

    @LocalServerPort
    private Integer port;

    @Value("classpath:responses/response1.json")
    private Resource jsonResponse;

    @AfterEach
    public void afterEach() {
        this.wireMockServer.resetAll();
        this.repositoryDetailsRepository.deleteAll();
    }

    @Test
    void testGetRepositoryDetailsOk() {

        this.wireMockServer.stubFor(
                WireMock.get("/repos/owner/name")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(asString(jsonResponse)))
        );

        this.webTestClient
                .get()
                .uri("http://localhost:" + port + "/repositories/owner/name")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("fullName").isEqualTo("test_owner/test_name")
                .jsonPath("description").isEqualTo("test_description")
                .jsonPath("cloneUrl").isEqualTo("test_clone_url")
                .jsonPath("starsNumber").isEqualTo("45724")
                .jsonPath("createdAt").isEqualTo("2010-12-08T04:04:45");

        Assertions.assertThat(repositoryDetailsRepository.count()).isEqualTo(1L);
    }

    @Test
    void testGetRepositoryDetailsNotFound() {

        this.wireMockServer.stubFor(
                WireMock.get("/repos/owner/name")
                        .willReturn(aResponse().withStatus(404)));

        this.webTestClient
                .get()
                .uri("http://localhost:" + port + "/repositories/owner/name")
                .exchange()
                .expectStatus().isNotFound().expectBody().isEmpty();

        Assertions.assertThat(repositoryDetailsRepository.count()).isEqualTo(0L);
    }

    @Test
    void testGetRepositoryDetailsOtherError(){

        this.wireMockServer.stubFor(
                WireMock.get("/repos/owner/name")
                        .willReturn(aResponse().withStatus(500)));

        this.webTestClient
                .get()
                .uri("http://localhost:" + port + "/repositories/owner/name")
                .exchange()
                .expectStatus().isNotFound().expectBody().isEmpty();

        Assertions.assertThat(repositoryDetailsRepository.count()).isEqualTo(0L);
    }

    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
