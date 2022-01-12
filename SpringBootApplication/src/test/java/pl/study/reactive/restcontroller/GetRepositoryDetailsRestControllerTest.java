package pl.study.reactive.restcontroller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.study.reactive.interactors.ObtainRepositoryDetailsInteractor;
import pl.study.reactive.model.RepositoryDetails;
import pl.study.reactive.ports.RequestRepositoryDetailsGateway;
import pl.study.reactive.storage.LoadRepositoryDetailsRepository;
import pl.study.reactive.storage.SaveRepositoryDetailsRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = GetRepositoryDetailsRestController.class)
@Import(ObtainRepositoryDetailsInteractor.class)
class GetRepositoryDetailsRestControllerTest {

    private static final RepositoryDetails REPOSITORY_DETAILS = new RepositoryDetails("owner", "repositoryName",
            "desc", "cloneUrl", 0, LocalDateTime.MIN);

    @MockBean
    LoadRepositoryDetailsRepository loadRepositoryDetailsRepository;
    @MockBean
    SaveRepositoryDetailsRepository saveRepositoryDetailsRepository;
    @MockBean
    RequestRepositoryDetailsGateway requestRepositoryDetailsGateway;

    @Autowired
    private WebTestClient webClient;

    @Test
    void testGetCachedRecord() {
        Mockito.when(loadRepositoryDetailsRepository.load("owner", "repositoryName"))
                .thenReturn(Mono.just(REPOSITORY_DETAILS));
        webClient.get()
                .uri("/repositories/owner/repositoryName")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"fullName\":\"owner/repositoryName\",\"description\":\"desc\",\"cloneUrl\":\"cloneUrl\",\"starsNumber\":0,\"createdAt\":\"-999999999-01-01T00:00:00\"}");

        Mockito.verify(loadRepositoryDetailsRepository, times(1))
                .load("owner", "repositoryName");
        Mockito.verifyNoInteractions(saveRepositoryDetailsRepository, requestRepositoryDetailsGateway);
    }

    @Test
    void testRequestForNewRecord() {
        Mockito.when(loadRepositoryDetailsRepository.load("owner", "repositoryName"))
                .thenReturn(Mono.empty());
        Mockito.when(requestRepositoryDetailsGateway.request("owner", "repositoryName"))
                .thenReturn(Mono.just(REPOSITORY_DETAILS));
        Mockito.when(saveRepositoryDetailsRepository.save(REPOSITORY_DETAILS))
                .thenReturn(Mono.just(REPOSITORY_DETAILS));
        webClient.get()
                .uri("/repositories/owner/repositoryName")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"fullName\":\"owner/repositoryName\",\"description\":\"desc\",\"cloneUrl\":\"cloneUrl\",\"starsNumber\":0,\"createdAt\":\"-999999999-01-01T00:00:00\"}");

        Mockito.verify(loadRepositoryDetailsRepository, times(1))
                .load("owner", "repositoryName");
        Mockito.verify(requestRepositoryDetailsGateway, times(1))
                .request("owner", "repositoryName");
        Mockito.verify(saveRepositoryDetailsRepository, times(1))
                .save(REPOSITORY_DETAILS);
    }
}