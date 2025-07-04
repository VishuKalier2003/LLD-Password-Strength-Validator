package checker.pwd.endpoints.api;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import checker.pwd.endpoints.dto.Data;
import checker.pwd.facade.Facade;

@RestController
@RequestMapping("/api")
public class Controller {
    
    @Autowired
    private Facade facade;

    @PostMapping("/password")
    public CompletableFuture<ResponseEntity<Map<String, String>>> test(@RequestBody Data data) {
        return facade.fTest(data.getData()).thenApply(map -> ResponseEntity.status(HttpStatus.ACCEPTED).body(map));
    }
}
