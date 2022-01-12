package pl.study.reactive.usecases;

import pl.study.reactive.model.RepositoryDetails;
import reactor.core.publisher.Mono;

public interface ObtainRepositoryDetailsUseCase {

    Mono<RepositoryDetails> get(String owner, String repositoryName);
}
