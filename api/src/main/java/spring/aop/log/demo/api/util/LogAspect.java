package spring.aop.log.demo.api.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    /**
     * 新增结果返回后触发
     *
     * @param point
     * @param returnValue
     */
    @AfterReturning(returning = "returnValue", pointcut = "operationLog() && @annotation(log)")
    public void doAfterReturning(JoinPoint point, Object returnValue, Log log) {
        try {
            // 获取请求详情
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            HttpServletResponse response = attributes.getResponse();
            // 实例化参数类
            Param param = new Param();
            // 注解中的类型
            String enumKey = log.type();
            String logDetail = Type.valueOf(enumKey).getOperation();

            // 从请求传入参数中获取数据
            this.getAndSetParam(point, param);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 获取拦截的请求中的参数
     * @param point
     * @param param
     */
    private void getAndSetParam(JoinPoint point, Param param) {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] reqParams = methodSignature.getParameterNames();
        // 获取参数
        Object[] args = point.getArgs();

        for (String reqParam : reqParams) {
            if (this.isExist(param.getClass(), reqParam)) {
                System.out.println("true");
            }
        }
    }

    /**
     * 将字符串的首字母大写
     *
     * @param str
     * @return
     */
    private String setFirstLetterUpperCase(String str) {
        if (str == null) {
            return null;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 判断该参数的构造类是否是基础类型
     * @param arg
     * @return
     */
    private Boolean isBasicType(Object arg) {
        Class argClass = arg.getClass();
        String keyword = "lang";
        return arg.toString().contains(keyword);
    }

    /**
     * 判断该参数在参数类中是否存在（是否是需要记录的参数）
     * @param targetClass
     * @param name
     * @param <T>
     * @return
     */
    private <T> Boolean isExist(T targetClass, String name) {
        boolean exist = false;
        try {
            String key = this.setFirstLetterUpperCase(name);
            Method targetClassGetMethod = targetClass.getClass().getMethod("get" + key);
        } catch (NoSuchMethodException e) {
            exist = true;
        }
        return exist;
    }

}
