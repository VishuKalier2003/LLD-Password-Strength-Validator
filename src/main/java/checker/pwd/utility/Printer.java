package checker.pwd.utility;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class Printer {
    
    public void printDash() {
        String x = "-".repeat(80);
        System.out.println(x);
    }

    public void printData(String s) {
        System.out.println(s);
    }

    public void mapData(Map<String, String> mp) {
        System.out.println(mp);
    }
}
