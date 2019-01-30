package spring.aop.log.demo.api.util;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * LogAspect
 *
 * @author Lunhao Hu
 * @date 2019-01-30 16:21
 **/
@Aspect
@Component
public class LogAspect {

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(spring.aop.log.demo.api.util.Log)")
    public void operationLog() {
    }
}
