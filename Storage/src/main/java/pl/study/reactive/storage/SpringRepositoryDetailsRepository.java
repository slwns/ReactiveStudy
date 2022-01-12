package pl.study.reactive.storage;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SpringRepositoryDetailsRepository extends CrudRepository<RepositoryDetailsEntity, Long> {

    Optional<RepositoryDetailsEntity> findByOwnerAndRepositoryName(String owner, String repositoryName);
}
