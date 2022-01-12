package pl.study.reactive.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.study.reactive.model.RepositoryDetails;
import pl.study.reactive.ports.LoadRepositoryDetailsGateway;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class LoadRepositoryDetailsRepository implements LoadRepositoryDetailsGateway {

    private static final Logger logger = LoggerFactory.getLogger(LoadRepositoryDetailsRepository.class);

    private final SpringRepositoryDetailsRepository springRepository;

    public LoadRepositoryDetailsRepository(SpringRepositoryDetailsRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Mono<RepositoryDetails> load(String owner, String repositoryName) {
        return Mono.fromCallable(() -> {
            logger.info("Loading entity...: " + owner + " " + repositoryName);
            return springRepository.findByOwnerAndRepositoryName(owner, repositoryName);
        })
                .flatMap(Mono::justOrEmpty)
                .doOnNext((e) -> logger.info("Found entity: " + e.getOwner() + " " + e.getRepositoryName()))
                .map(RepositoryDetailsEntity::toDomainModel).subscribeOn(Schedulers.boundedElastic())
                .switchIfEmpty(Mono.defer(() -> {
                    logger.info("Entity not found");
                    return Mono.empty();
                }));
    }
}
