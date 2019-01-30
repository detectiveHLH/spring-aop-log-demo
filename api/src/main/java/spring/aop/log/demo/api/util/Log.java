package spring.aop.log.demo.api.util;

        import java.lang.annotation.ElementType;
        import java.lang.annotation.Retention;
        import java.lang.annotation.RetentionPolicy;
        import java.lang.annotation.Target;

/**
 * Log
 *
 * @author Lunhao Hu
 * @date 2019-01-30 16:19
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    String type();
}
