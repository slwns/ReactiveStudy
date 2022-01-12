package pl.study.reactive.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.study.reactive.model.RepositoryDetails;
import pl.study.reactive.ports.SaveRepositoryDetailsGateway;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class SaveRepositoryDetailsRepository implements SaveRepositoryDetailsGateway {

    private static final Logger logger = LoggerFactory.getLogger(SaveRepositoryDetailsRepository.class);

    private final SpringRepositoryDetailsRepository springRepository;

    public SaveRepositoryDetailsRepository(SpringRepositoryDetailsRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Mono<RepositoryDetails> save(RepositoryDetails repositoryDetails) {
        return Mono.fromCallable(() -> {
            logger.info("Saving entity: " + repositoryDetails.getOwner() + " " + repositoryDetails.getRepositoryName());
            return springRepository.save(RepositoryDetailsEntity.fromDomainModel(repositoryDetails));
        })
                .flatMap(Mono::justOrEmpty).map(RepositoryDetailsEntity::toDomainModel).subscribeOn(Schedulers.boundedElastic());
    }
}
