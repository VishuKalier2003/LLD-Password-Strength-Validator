package checker.pwd.domain.model;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import checker.pwd.domain.annotate.RuleMeta;
import checker.pwd.domain.interfaces.Testing;
import checker.pwd.utility.Printer;

@Component
@RuleMeta(ruleName = "VARIETY", order = 2, necessary = true, waitTime=2000)
public class VarietyRule implements Testing {

    @Autowired
    private Printer printer;

    @Override
    public CompletableFuture<String> ruleApply(String password, Map<String, String> holder) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                printer.printData("VARIETY check started for "+password+" successfully...");
                boolean flag[] = new boolean[4];
                int n = password.length();
                Thread.sleep(this.getClass().getAnnotation(RuleMeta.class).waitTime());
                for (int i = 0; i < n; i++) {
                    char ch = password.charAt(i);
                    if (Character.isDigit(ch))
                        flag[0] = true;
                    else if (Character.isLetter(ch) && Character.isLowerCase(ch))
                        flag[1] = true;
                    else if (Character.isLetter(ch) && Character.isUpperCase(ch))
                        flag[2] = true;
                    else
                        flag[3] = true;
                }
                int count = 0;
                for (int i = 0; i < 4; i++)
                    count += flag[i] ? 1 : 0;
                switch (count) {
                    case 0 -> holder.put(this.getClass().getAnnotation(RuleMeta.class).ruleName(), "Poor");
                    case 1 -> holder.put(this.getClass().getAnnotation(RuleMeta.class).ruleName(), "Weak");
                    case 2 ->
                        holder.put(this.getClass().getAnnotation(RuleMeta.class).ruleName(), "Moderate");
                    default -> holder.put(this.getClass().getAnnotation(RuleMeta.class).ruleName(), "Good");
                }
            } catch (InterruptedException e) {
                holder.put("ERROR in Variety", e.getLocalizedMessage());
            } finally {
                printer.printData("VARIEY check ended for "+password+" successfully...");
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
