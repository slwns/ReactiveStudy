package pl.study.reactive.webclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.study.reactive.model.RepositoryDetails;
import pl.study.reactive.ports.RequestRepositoryDetailsGateway;
import reactor.core.publisher.Mono;

@Service
public class RequestRepositoryDetailsWebClient implements RequestRepositoryDetailsGateway {

    private static final Logger logger = LoggerFactory.getLogger(RequestRepositoryDetailsWebClient.class);

    private final WebClient webClient;

    public RequestRepositoryDetailsWebClient(WebClient.Builder webClientBuilder, @Value("${github.base.url}") String githubBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(githubBaseUrl).build();
    }

    @Override
    public Mono<RepositoryDetails> request(String owner, String repositoryName) {
        return this.webClient.get().uri("/repos/{owner}/{repositoryName}", owner, repositoryName)
                .retrieve()
                .onStatus(HttpStatus::isError, response -> {
                    logger.info("Error from GITHUB: " + response.rawStatusCode());
                    return Mono.error(new IllegalStateException());
                })
                .bodyToMono(RepositoryDetailsDto.class)
                .doOnNext((r) -> logger.info("Received GITHUB response..." + r))
                .map(RepositoryDetailsDto::toDomainModel);
    }
}
