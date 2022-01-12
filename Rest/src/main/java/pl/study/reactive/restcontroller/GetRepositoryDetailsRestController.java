package pl.study.reactive.restcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.study.reactive.usecases.ObtainRepositoryDetailsUseCase;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@RestController
@RequestMapping("/repositories")
public class GetRepositoryDetailsRestController {

    private final ObtainRepositoryDetailsUseCase obtainRepositoryDetailsUseCase;

    public GetRepositoryDetailsRestController(ObtainRepositoryDetailsUseCase obtainRepositoryDetailsUseCase) {
        this.obtainRepositoryDetailsUseCase = obtainRepositoryDetailsUseCase;
    }

    @GetMapping("/{owner}/{repositoryName}")
    public Mono<ResponseEntity<GetRepositoryDetailsResponse>> getRepositoryDetails(@PathVariable String owner, @PathVariable String repositoryName) {
        return obtainRepositoryDetailsUseCase.get(owner, repositoryName)
                .map(r -> ResponseEntity.ok(GetRepositoryDetailsResponse.fromDomainModel(r)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
