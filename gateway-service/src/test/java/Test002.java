import org.example.feign.UserWebClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/9/16
 **/
@ExtendWith(MockitoExtension.class)
public class Test002 {
    @Mock
    private WebClient.Builder webClientBuilder;
    @InjectMocks
    private UserWebClient userWebClient;

    @Test
    public void test001() {
        Mockito.when(webClientBuilder.build()).thenReturn(null);
        Assertions.assertNull(webClientBuilder.build());
    }
}
