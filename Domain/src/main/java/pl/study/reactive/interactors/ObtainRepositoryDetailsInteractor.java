package pl.study.reactive.interactors;

import pl.study.reactive.model.RepositoryDetails;
import pl.study.reactive.ports.LoadRepositoryDetailsGateway;
import pl.study.reactive.ports.RequestRepositoryDetailsGateway;
import pl.study.reactive.ports.SaveRepositoryDetailsGateway;
import pl.study.reactive.usecases.ObtainRepositoryDetailsUseCase;
import reactor.core.publisher.Mono;

public class ObtainRepositoryDetailsInteractor implements ObtainRepositoryDetailsUseCase {

    private final RequestRepositoryDetailsGateway requestRepositoryDetailsGateway;
    private final LoadRepositoryDetailsGateway loadRepositoryDetailsGateway;
    private final SaveRepositoryDetailsGateway saveRepositoryDetailsGateway;

    public ObtainRepositoryDetailsInteractor(RequestRepositoryDetailsGateway requestRepositoryDetailsGateway, LoadRepositoryDetailsGateway loadRepositoryDetailsGateway, SaveRepositoryDetailsGateway saveRepositoryDetailsGateway) {
        this.requestRepositoryDetailsGateway = requestRepositoryDetailsGateway;
        this.loadRepositoryDetailsGateway = loadRepositoryDetailsGateway;
        this.saveRepositoryDetailsGateway = saveRepositoryDetailsGateway;
    }

    @Override
    public Mono<RepositoryDetails> get(String owner, String repositoryName) {
        return loadRepositoryDetailsGateway.load(owner, repositoryName)
                .onErrorResume(e -> Mono.empty())
                .switchIfEmpty(Mono.defer(() -> requestRepositoryDetailsGateway.request(owner, repositoryName)
                        .onErrorResume(e -> Mono.empty())
                        .doOnNext(rd -> saveRepositoryDetailsGateway.save(rd).subscribe())));
    }
}
