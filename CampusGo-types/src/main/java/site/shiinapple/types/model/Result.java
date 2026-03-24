package site.shiinapple.types.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应结构 (Lombok 优化版)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应码 (0表示成功，其他表示失败)
     */
    private Integer code;

    /**
     * 响应消息 (如 "ok" 或具体错误信息)
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 成功静态方法 (携带数据)
     * 严格对齐文档：code = 0, message = "ok"
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(0, "ok", data);
    }

    /**
     * 成功静态方法 (无数据)
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 失败静态方法 (未登录专用)
     * 严格对齐文档：code = 40100
     */
    public static <T> Result<T> unLogin() {
        return new Result<>(40100, "未登录/Token 无效", null);
    }

    /**
     * 失败静态方法 (自定义错误码和消息)
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}