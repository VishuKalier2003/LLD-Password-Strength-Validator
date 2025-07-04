package checker.pwd.config;

import java.util.Comparator;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import checker.pwd.domain.annotate.RuleMeta;
import checker.pwd.domain.interfaces.Testing;

@Configuration
public class ValidatorChain {

    private final List<Testing> chain;

    // Spring DI automatically passes all beans that implement the testing interface
    public ValidatorChain(List<Testing> rules) {
        this.chain = rules;
    }

    @Bean
    public List<Testing> chaining() {
        return chain.stream().
        // sorting on basis of order value provided in RuleMeta annotation (ascending order)
        sorted(Comparator.comparingInt(rule -> {
            RuleMeta mt = rule.getClass().getAnnotation(RuleMeta.class);
            return mt.order();
        })).toList();
    }
}
