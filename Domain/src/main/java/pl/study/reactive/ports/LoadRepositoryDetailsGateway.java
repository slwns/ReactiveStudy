package pl.study.reactive.ports;

import pl.study.reactive.model.RepositoryDetails;
import reactor.core.publisher.Mono;

public interface LoadRepositoryDetailsGateway {

    Mono<RepositoryDetails> load(String owner, String repositoryName);
}
