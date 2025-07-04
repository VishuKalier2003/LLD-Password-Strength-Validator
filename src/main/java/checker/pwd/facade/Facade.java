package checker.pwd.facade;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import checker.pwd.service.TestService;

@Service
public class Facade {

    @Autowired
    private TestService testService;
    
    public CompletableFuture<Map<String, String>> fTest(String password) {
        return testService.fullTesting(password);
    }
}
