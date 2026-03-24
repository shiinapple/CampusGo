package site.shiinapple.types.model;

import java.io.Serializable;

/**
 * 统一响应结构
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    public Result() {
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功静态方法
     * @param data 数据
     * @param <T>  数据类型
     * @return Result
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(0, "ok", data);
    }

    /**
     * 成功静态方法（无数据）
     * @return Result
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 失败静态方法
     * @param code    错误码
     * @param message 错误消息
     * @return Result
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<T>(code, message, null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
