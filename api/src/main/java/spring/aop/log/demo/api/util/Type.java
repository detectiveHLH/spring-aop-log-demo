package spring.aop.log.demo.api.util;

/**
 * Type
 *
 * @author Lunhao Hu
 * @date 2019-01-30 17:12
 **/
public enum Type {

    /**
     * 操作类型
     */
    WARNING("警告", "因 工单号 [(%workOrderId)] /举报 ID [(%id)] 警告玩家 [(%userId)]");

    /**
     * 类型
     */
    private String type;

    /**
     * 执行操作
     */
    private String operation;

    Type(String type, String operation) {
        this.type = type;
        this.operation = operation;
    }

    public String getType() { return this.type; }

    public String getOperation() { return this.operation; }
}
