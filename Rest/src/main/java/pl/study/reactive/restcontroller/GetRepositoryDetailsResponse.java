package pl.study.reactive.restcontroller;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.study.reactive.model.RepositoryDetails;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GetRepositoryDetailsResponse {

    private final String fullName;
    private String description;
    private String cloneUrl;
    private Integer starsNumber;
    private LocalDateTime createdAt;

    public static GetRepositoryDetailsResponse fromDomainModel(RepositoryDetails repositoryDetails) {
        return new GetRepositoryDetailsResponse(repositoryDetails.getOwner() + "/" + repositoryDetails.getRepositoryName(),
                repositoryDetails.getDescription(), repositoryDetails.getCloneUrl(), repositoryDetails.getStarsNumber(),
                repositoryDetails.getCreatedAt());
    }
}
