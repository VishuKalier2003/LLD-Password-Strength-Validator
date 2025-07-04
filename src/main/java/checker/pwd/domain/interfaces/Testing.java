package checker.pwd.domain.interfaces;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface Testing {
    public CompletableFuture<String> ruleApply(String password, Map<String, String> holder);

    public String ruleName();
}
