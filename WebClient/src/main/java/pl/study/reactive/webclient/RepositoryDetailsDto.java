package pl.study.reactive.webclient;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.study.reactive.model.RepositoryDetails;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RepositoryDetailsDto {

    private Owner owner;
    @JsonProperty(value = "name")
    private String repositoryName;
    private String description;
    @JsonProperty(value = "clone_url")
    private String cloneUrl;
    @JsonProperty(value = "stargazers_count")
    private Integer starsNumber;
    @JsonProperty(value = "created_at")
    private LocalDateTime createdAt;

    @Data
    public static class Owner {
        private String login;
    }

    public RepositoryDetails toDomainModel() {
        return new RepositoryDetails(owner.getLogin(), repositoryName, description, cloneUrl, starsNumber, createdAt);
    }
}
