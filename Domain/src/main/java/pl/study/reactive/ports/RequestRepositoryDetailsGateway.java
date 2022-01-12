package pl.study.reactive.ports;

import pl.study.reactive.model.RepositoryDetails;
import reactor.core.publisher.Mono;

public interface RequestRepositoryDetailsGateway {

    Mono<RepositoryDetails> request(String owner, String repositoryName);
}
