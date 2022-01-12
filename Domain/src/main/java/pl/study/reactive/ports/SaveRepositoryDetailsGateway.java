package pl.study.reactive.ports;

import pl.study.reactive.model.RepositoryDetails;
import reactor.core.publisher.Mono;

public interface SaveRepositoryDetailsGateway {

    Mono<RepositoryDetails> save(RepositoryDetails repositoryDetails);
}
