package spring.aop.log.demo.api.util;

import cn.hutool.core.util.ArrayUtil;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
     * 请求中的所有参数
     */
    private Object[] args;

    /**
     * 请求中的所有参数名
     */
    private String[] paramNames;

    /**
     * 参数类
     */
    private Param params;


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
            // 获取所有请求参数
            Signature signature = point.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            this.paramNames = methodSignature.getParameterNames();
            this.args = point.getArgs();

            // 实例化参数类
            this.params = new Param();
            // 注解中的类型
            String enumKey = log.type();
            String logDetail = Type.valueOf(enumKey).getOperation();

            // 从请求传入参数中获取数据
            this.getRequestParam(point);

            if (!logDetail.isEmpty()) {
                // 将模板中的参数全部替换掉
                logDetail = this.replaceParam(logDetail);
            }

            System.out.println(logDetail);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 获取拦截的请求中的参数
     * @param point
     */
    private void getRequestParam(JoinPoint point) {
        // 获取简单参数类型
        this.getSimpleParam();
    }

    /**
     * 获取简单参数类型的值
     */
    private void getSimpleParam() {
        // 遍历请求中的参数名
        for (String reqParam : this.paramNames) {
            // 判断该参数在参数类中是否存在
            if (this.isExist(this.params.getClass(), reqParam)) {
                this.setRequestParamValueIntoParam(reqParam);
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
        return argClass.toString().contains(keyword);
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

    /**
     * 将数据写入参数类的实例中
     * @param targetClass
     * @param key
     * @param value
     * @param <T>
     */
    private <T> void setParam(T targetClass, String key, String value) {
        try {
            Method targetClassParamSetMethod = targetClass.getClass().getMethod("set" + this.setFirstLetterUpperCase(key), String.class);
            targetClassParamSetMethod.invoke(targetClass, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过反射获取传入的类中对应key的值
     * @param targetClass
     * @param key
     * @param <T>
     */
    private <T> String getParam(T targetClass, String key) {
        String value = "";
        try {
            Method targetClassParamGetMethod = targetClass.getClass().getMethod("get" + this.setFirstLetterUpperCase(key));
            value = String.valueOf(targetClassParamGetMethod.invoke(targetClass));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 从参数中获取
     * @param paramName
     * @return
     */
    private void setRequestParamValueIntoParam(String paramName) {
        int index = ArrayUtil.indexOf(this.paramNames, paramName);
        if (index != -1) {
            String value = String.valueOf(this.args[index]);
            this.setParam(this.params, paramName, value);
        }
    }

    /**
     * 将模板中的预留字段全部替换为拦截到的参数
     * @param template
     * @return
     */
    private String replaceParam(String template) {
        // 将模板中的需要替换的参数转化成map
        Map<String, String> paramsMap = this.convertToMap(template);
        for (String key : paramsMap.keySet()) {
            template = template.replace("%" + key, paramsMap.get(key)).replace("(", "").replace(")", "");
        }
        return template;
    }

    /**
     * 将模板中的参数转换成map的key-value形式
     * @param template
     * @return
     */
    private Map<String, String> convertToMap(String template) {
        Map<String, String> map = new HashMap<>();
        String[] arr = template.split("\\(");
        for (String s : arr) {
            if (s.contains("%")) {
                String key = s.substring(s.indexOf("%"), s.indexOf(")")).replace("%", "").replace(")", "").replace("-", "").replace("]", "");
                String value = this.getParam(this.params, key);
                map.put(key, "null".equals(value) ? "(空)" : value);
            }
        }
        return map;
    }
}
