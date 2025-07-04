package checker.pwd.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import checker.pwd.domain.annotate.RuleMeta;
import checker.pwd.domain.interfaces.Testing;
import checker.pwd.utility.Printer;

@Component
@RuleMeta(ruleName = "COMMON", order = 4, necessary = true, waitTime = 2000)
public class CommonRule implements Testing {

    @Autowired
    private Printer printer;

    @Override
    public CompletableFuture<String> ruleApply(String password, Map<String, String> holder) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                printer.printData("COMMON check started for " + password + " successfully...");
                List<String> segments = segmentation(password);
                Thread.sleep(this.getClass().getAnnotation(RuleMeta.class).waitTime());
                boolean flag = true;
                for (String segment : segments) {
                    if (!check(segment)) {
                        holder.put(this.getClass().getAnnotation(RuleMeta.class).ruleName(), "Repeated");
                        flag = false;
                        break;
                    }
                }
                if (flag)
                    holder.put(this.getClass().getAnnotation(RuleMeta.class).ruleName(), "Neat");
            } catch (InterruptedException e) {
                holder.put("ERROR in Common ", e.getLocalizedMessage());
            } finally {
                printer.printData("COMMON check ended for " + password + " successfully...");
                printer.mapData(holder);
            }
            return password;
        });
    }

    @Override
    public String ruleName() {
        return this.getClass().getAnnotation(RuleMeta.class).ruleName();
    }

    private List<String> segmentation(String s) {
        List<String> segments = new ArrayList<>();
        if (s == null || s.isEmpty()) {
            return segments;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(s.charAt(0));
        for (int j = 1; j < s.length(); j++) {
            char prev = sb.charAt(sb.length() - 1);
            char current = s.charAt(j);
            // same type? digit vs. letter
            if (Character.isDigit(prev) == Character.isDigit(current)) {
                sb.append(current);
            } else {
                // type changed → flush the old segment
                segments.add(sb.toString());
                sb.setLength(0);
                sb.append(current);
            }
        }
        // don't forget the last segment
        segments.add(sb.toString());
        return segments;
    }

    private boolean check(String s) {
        int n = s.length();
        if (Character.isDigit(s.charAt(0))) {
            for (int i = 0; i < n - 1; i++)
                if (Math.abs(s.charAt(i) - '0' - s.charAt(i + 1) - '0') == 1)
                    return false;
            return true;
        } else {
            for (int i = 0; i < n - 1; i++)
                if (s.charAt(i) == s.charAt(i + 1))
                    return false;
            return true;
        }
    }
}
