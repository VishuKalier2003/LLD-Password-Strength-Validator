package checker.pwd.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import checker.pwd.config.ValidatorChain;
import checker.pwd.domain.annotate.RuleMeta;
import checker.pwd.domain.interfaces.Testing;


@Service
public class TestService {
    
    @Autowired
    private ValidatorChain validator;

    public CompletableFuture<Map<String, String>> fullTesting(String password) {
        Map<String, String> map = new ConcurrentHashMap<>();
        List<Testing> chain = validator.chaining();
        CompletableFuture<String> pipeline = CompletableFuture.completedFuture(password);
        for(Testing rule : chain)
            pipeline = pipeline.thenCompose(pwd -> rule.ruleApply(pwd, map));
        return pipeline.thenApply(ignored -> map);
    }

    public CompletableFuture<Map<String, String>> partialTesting(String password) {
        Map<String, String> map = new ConcurrentHashMap<>();
        List<Testing> chain = validator.chaining();
        CompletableFuture<String> pipeline = CompletableFuture.completedFuture(password);
        for(Testing rule : chain) {
            if(rule.getClass().getAnnotation(RuleMeta.class).necessary())
                pipeline = pipeline.thenCompose(pwd -> rule.ruleApply(pwd, map));       // reassign at every instance
        }
        return pipeline.thenApply(ignored -> map);
    }
}
