package checker.pwd.domain.model;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import checker.pwd.domain.annotate.RuleMeta;
import checker.pwd.domain.interfaces.Testing;
import checker.pwd.utility.Printer;

@Component
@RuleMeta(ruleName = "LENGTH", order = 1, necessary = true, waitTime=1500)
public class LengthRule implements Testing {
    @Autowired
    private Printer printer;

    @Override
    public CompletableFuture<String> ruleApply(String password, Map<String, String> holder) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                printer.printDash();
                printer.printData("LENGTH check started for "+password+" successfully...");
                Thread.sleep(this.getClass().getAnnotation(RuleMeta.class).waitTime());
                int k = password.length();
                if(k < 8) {
                    holder.put(this.getClass().getAnnotation(RuleMeta.class).ruleName(), "Weak");
                } else if(k < 16) {
                    holder.put(this.getClass().getAnnotation(RuleMeta.class).ruleName(), "Medium");
                } else {
                    holder.put(this.getClass().getAnnotation(RuleMeta.class).ruleName(), "High");
                }
            } catch(InterruptedException e) {
                holder.put("ERROR", e.getLocalizedMessage());
            } finally {
                printer.printDash();
                printer.printData("LENGTH check ended for "+password+" successfully...");
                printer.printData("Holder");
                printer.mapData(holder);
            }
            return password;
        });
    }

    @Override
    public String ruleName() {
        return this.getClass().getAnnotation(RuleMeta.class).ruleName();
    }
}
