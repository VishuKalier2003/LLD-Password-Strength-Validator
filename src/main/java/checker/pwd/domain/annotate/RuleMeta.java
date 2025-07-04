package checker.pwd.domain.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RuleMeta {
    String ruleName() default "";
    int order() default Integer.MAX_VALUE;
    boolean necessary() default true;
    int waitTime() default 2000;
}
