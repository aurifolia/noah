import org.example.dto.ResultDTO;
import org.example.dto.ServiceInfo;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/9/16
 **/
public class Test001 {
    public static void main(String[] args) throws InterruptedException {
        Map<String, AtomicInteger> threadNameMap = new ConcurrentHashMap<>();
        ExecutorService pool = Executors.newFixedThreadPool(30);
        int times = 10000;
        for (int i = 0; i < times; i++) {
            pool.execute(() -> {
                RestTemplate restTemplate = new RestTemplate();
                String uuid = UUID.randomUUID().toString();
                Map<String, Object> result = null;
                try {
                    result = (Map<String, Object>) restTemplate.getForObject("http://localhost:60200/v1/func001?uuid=" + uuid, ResultDTO.class).getData();
                } catch (Exception e) {
                    return;
                }
                if (!String.format("[%s]", uuid).equals(((Map) result.get("params")).get("uuid"))) {
                    System.out.println("failed");
                }
//                threadNameMap.computeIfAbsent((String) result.get("thread"), key -> new AtomicInteger()).incrementAndGet();
            });
        }
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.DAYS);
        System.out.println("finish");
        threadNameMap.forEach((k, v) -> {
            System.out.printf("%s -- > %s%n", k, v.get());
        });
    }
}
