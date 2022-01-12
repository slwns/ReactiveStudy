package pl.study.reactive.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RepositoryDetails {

    private final String owner;
    private final String repositoryName;
    private final String description;
    private final String cloneUrl;
    private final Integer starsNumber;
    private final LocalDateTime createdAt;
}
