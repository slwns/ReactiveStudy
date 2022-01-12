package pl.study.reactive.interactors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.study.reactive.model.RepositoryDetails;
import pl.study.reactive.ports.LoadRepositoryDetailsGateway;
import pl.study.reactive.ports.RequestRepositoryDetailsGateway;
import pl.study.reactive.ports.SaveRepositoryDetailsGateway;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObtainRepositoryDetailsInteractorTest {

    private static final RepositoryDetails REPOSITORY_DETAILS = new RepositoryDetails("owner", "repoName",
            "desc", "cloneUrl", 0, LocalDateTime.now());

    @Mock
    LoadRepositoryDetailsGateway loadMock;
    @Mock
    RequestRepositoryDetailsGateway requestMock;
    @Mock
    SaveRepositoryDetailsGateway saveMock;
    @InjectMocks
    ObtainRepositoryDetailsInteractor cut;

    @Test
    void testGetFromLoadSuccess() {
        when(loadMock.load(anyString(), anyString())).thenReturn(Mono.just(REPOSITORY_DETAILS));

        StepVerifier
                .create(cut.get("owner", "repositoryName").log())
                .expectNext(REPOSITORY_DETAILS)
                .expectComplete()
                .verify();

        verify(loadMock, times(1)).load("owner", "repositoryName");
        verifyNoInteractions(requestMock, saveMock);
    }

    @Test
    void testGetFromLoadEmpty_RequestSuccess() {
        when(loadMock.load(anyString(), anyString())).thenReturn(Mono.empty());
        when(requestMock.request(anyString(), anyString())).thenReturn(Mono.just(REPOSITORY_DETAILS));
        when(saveMock.save(REPOSITORY_DETAILS)).thenReturn(Mono.just(REPOSITORY_DETAILS));

        StepVerifier
                .create(cut.get("owner", "repositoryName").log())
                .expectNext(REPOSITORY_DETAILS)
                .expectComplete()
                .verify();

        verify(loadMock, times(1)).load("owner", "repositoryName");
        verify(requestMock, times(1)).request("owner", "repositoryName");
        verify(saveMock, times(1)).save(REPOSITORY_DETAILS);
    }

    @Test
    void testGetFromLoadError_RequestSuccess() {
        when(loadMock.load(anyString(), anyString())).thenReturn(Mono.error(new RuntimeException()));
        when(requestMock.request(anyString(), anyString())).thenReturn(Mono.just(REPOSITORY_DETAILS));
        when(saveMock.save(REPOSITORY_DETAILS)).thenReturn(Mono.just(REPOSITORY_DETAILS));

        StepVerifier
                .create(cut.get("owner", "repositoryName").log())
                .expectNext(REPOSITORY_DETAILS)
                .expectComplete()
                .verify();

        verify(loadMock, times(1)).load("owner", "repositoryName");
        verify(requestMock, times(1)).request("owner", "repositoryName");
        verify(saveMock, times(1)).save(REPOSITORY_DETAILS);
    }

    @Test
    void testGetFromLoadEmpty_RequestEmpty() {
        when(loadMock.load(anyString(), anyString())).thenReturn(Mono.empty());
        when(requestMock.request(anyString(), anyString())).thenReturn(Mono.empty());

        StepVerifier
                .create(cut.get("owner", "repositoryName").log())
                .expectNextCount(0)
                .verifyComplete();

        verify(loadMock, times(1)).load("owner", "repositoryName");
        verify(requestMock, times(1)).request("owner", "repositoryName");
        verifyNoInteractions(saveMock);
    }

    @Test
    void testGetFromLoadEmpty_RequestError() {
        when(loadMock.load(anyString(), anyString())).thenReturn(Mono.empty());
        when(requestMock.request(anyString(), anyString())).thenReturn(Mono.error(new RuntimeException()));

        StepVerifier
                .create(cut.get("owner", "repositoryName").log())
                .expectNextCount(0)
                .verifyComplete();

        verify(loadMock, times(1)).load("owner", "repositoryName");
        verify(requestMock, times(1)).request("owner", "repositoryName");
        verifyNoInteractions(saveMock);
    }

    @Test
    void testGetFromLoadEmpty_RequestSuccess_SaveEmpty() {
        when(loadMock.load(anyString(), anyString())).thenReturn(Mono.error(new RuntimeException()));
        when(requestMock.request(anyString(), anyString())).thenReturn(Mono.just(REPOSITORY_DETAILS));
        when(saveMock.save(REPOSITORY_DETAILS)).thenReturn(Mono.empty());

        StepVerifier
                .create(cut.get("owner", "repositoryName").log())
                .expectNext(REPOSITORY_DETAILS)
                .expectComplete()
                .verify();

        verify(loadMock, times(1)).load("owner", "repositoryName");
        verify(requestMock, times(1)).request("owner", "repositoryName");
        verify(saveMock, times(1)).save(REPOSITORY_DETAILS);
    }

    @Test
    void testGetFromLoadEmpty_RequestSuccess_SaveError() throws InterruptedException {
        when(loadMock.load(anyString(), anyString())).thenReturn(Mono.error(new RuntimeException()));
        when(requestMock.request(anyString(), anyString())).thenReturn(Mono.just(REPOSITORY_DETAILS));
        when(saveMock.save(REPOSITORY_DETAILS)).thenReturn(Mono.error(new RuntimeException()));

        StepVerifier
                .create(cut.get("owner", "repositoryName").log())
                .expectNext(REPOSITORY_DETAILS)
                .expectComplete()
                .verify();

        verify(loadMock, times(1)).load("owner", "repositoryName");
        verify(requestMock, times(1)).request("owner", "repositoryName");
        verify(saveMock, times(1)).save(REPOSITORY_DETAILS);
    }
}