package spring.aop.log.demo.api.util;

import lombok.Data;

/**
 * Param
 *
 * @author Lunhao Hu
 * @date 2019-01-30 17:14
 **/
@Data
public class Param {
    /**
     * 所有可能参数
     */
    private String id;
    private String age;
    private String workOrderNumber;
    private String userId;
    private String name;
    private String email;
}
