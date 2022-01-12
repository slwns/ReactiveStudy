package pl.study.reactive.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.study.reactive.model.RepositoryDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryDetailsEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String owner;
    private String repositoryName;
    private String description;
    private String cloneUrl;
    private Integer starsNumber;
    private LocalDateTime createdAt;

    public static RepositoryDetailsEntity fromDomainModel(RepositoryDetails repositoryDetails) {
        return new RepositoryDetailsEntity(null, repositoryDetails.getOwner(),
                repositoryDetails.getRepositoryName(), repositoryDetails.getDescription(),
                repositoryDetails.getCloneUrl(), repositoryDetails.getStarsNumber(), repositoryDetails.getCreatedAt());
    }

    public RepositoryDetails toDomainModel() {
        return new RepositoryDetails(owner, repositoryName, description, cloneUrl, starsNumber, createdAt);
    }
}
