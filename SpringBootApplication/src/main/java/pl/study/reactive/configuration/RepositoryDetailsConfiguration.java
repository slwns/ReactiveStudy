package pl.study.reactive.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.study.reactive.interactors.ObtainRepositoryDetailsInteractor;
import pl.study.reactive.ports.LoadRepositoryDetailsGateway;
import pl.study.reactive.ports.RequestRepositoryDetailsGateway;
import pl.study.reactive.ports.SaveRepositoryDetailsGateway;
import pl.study.reactive.usecases.ObtainRepositoryDetailsUseCase;

@Configuration
@AllArgsConstructor
public class RepositoryDetailsConfiguration {

    private LoadRepositoryDetailsGateway loadRepositoryDetailsGateway;
    private SaveRepositoryDetailsGateway saveRepositoryDetailsGateway;
    private RequestRepositoryDetailsGateway requestRepositoryDetailsGateway;

    @Bean
    public ObtainRepositoryDetailsUseCase getObtainRepositoryDetailsUseCase() {
        return new ObtainRepositoryDetailsInteractor(requestRepositoryDetailsGateway, loadRepositoryDetailsGateway, saveRepositoryDetailsGateway);
    }
}
