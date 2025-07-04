package checker.pwd.domain.model;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import checker.pwd.domain.annotate.RuleMeta;
import checker.pwd.domain.interfaces.Testing;
import checker.pwd.utility.Printer;

@Component
@RuleMeta(ruleName = "ENTROPY", order = 3, necessary = false, waitTime=4000)
public class EntropyRule implements Testing {

    @Autowired
    private Printer printer;

    @Override
    public CompletableFuture<String> ruleApply(String password, Map<String, String> holder) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                printer.printData("ENTROPY check started for " + password + " successfully...");
                Thread.sleep(this.getClass().getAnnotation(RuleMeta.class).waitTime());
                double dx = calculateEntropy(password);
                holder.put(this.getClass().getAnnotation(RuleMeta.class).ruleName(), String.valueOf(dx));
            } catch (InterruptedException e) {
                holder.put("ERROR in Entropy", e.getLocalizedMessage());
            } finally {
                printer.printData("ENTROPY check ended for "+password+" successfully...");
                printer.mapData(holder);
            }
            return password;
        });
    }

    @Override
    public String ruleName() {
        return this.getClass().getAnnotation(RuleMeta.class).ruleName();
    }

    public double calculateEntropy(String password) {
        int charsetSize = 0;
        if (password.matches(".*[a-z].*"))
            charsetSize += 26;
        if (password.matches(".*[A-Z].*"))
            charsetSize += 26;
        if (password.matches(".*\\d.*"))
            charsetSize += 10;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"))
            charsetSize += 32;
        if (charsetSize == 0)
            return 0.0;
        return password.length() * (Math.log(charsetSize) / Math.log(2)); // log₂(R)
    }
}
