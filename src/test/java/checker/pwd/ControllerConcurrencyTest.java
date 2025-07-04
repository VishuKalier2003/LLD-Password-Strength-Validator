package checker.pwd;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ControllerConcurrencyTest {

    // Target the locally‐running server on port 8080
    private static final String URL = "http://localhost:8080/api/password";
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void whenMultiplePasswordsSubmittedConcurrently_thenAllResponsesAreAccepted() {
        // Prepare three distinct test passwords
        List<String> passwords = List.of(
            "TestPassword123",                        // expect "Weak"
            "pass12",                   // expect "Medium"
            "@a1~dfG4819CfraqZXPl17"  // expect "High"
        );

        // Fire off one POST per password, all in parallel
        List<CompletableFuture<ResponseEntity<Map<String,String>>>> futures = passwords.stream()
            .map(pwd -> CompletableFuture.supplyAsync(() -> {
                // Build JSON body: {"data":"<password>"}
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                Map<String,String> body = Map.of("data", pwd);
                HttpEntity<Map<String,String>> request = new HttpEntity<>(body, headers);

                // Exchange with explicit ParameterizedTypeReference
                return restTemplate.exchange(
                    URL,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, String>>() {}
                );
            }))
            .collect(Collectors.toList());

        // Wait for all of them to finish
        CompletableFuture.allOf(futures.toArray(CompletableFuture<?>[]::new)).join();

        // Verify each response is 202 ACCEPTED with a non‐empty map
        for (int i = 0; i < passwords.size(); i++) {
            ResponseEntity<Map<String,String>> resp = futures.get(i).join();
            assertEquals(HttpStatus.ACCEPTED, resp.getStatusCode(),
                         "Expected 202 ACCEPTED for password=" + passwords.get(i));
            assertNotNull(resp.getBody(), "Body should not be null");
            assertFalse(resp.getBody().isEmpty(),
                        "Body map should contain at least one rule result");
            System.out.println("Response for [" + passwords.get(i) + "] → " + resp.getBody());
        }
    }
}
