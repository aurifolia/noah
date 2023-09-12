import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/5/28
 **/
public class Test001 {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> entity = restTemplate.postForEntity("http://localhost:8080/test/002", null, String.class);
        System.out.println();
    }
}
