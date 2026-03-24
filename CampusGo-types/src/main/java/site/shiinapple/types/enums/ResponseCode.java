package site.shiinapple.types.enums;

public enum ResponseCode {

    /**
     * 0：成功
     * 40001：参数错误
     * 40100：未登录/Token 无效（HTTP status 也可为 401）
     * 40300：无权限
     * 40400：资源不存在
     * 50000：服务器内部错误
     * 前端在遇到 HTTP 401 时会清理本地登录态并引导重新登录。
     */

    SUCCESS("0", "成功"),
    UN_ERROR("40001", "参数错误"),
    UN_LOGIN("40100","未登录/Token 无效"),
    UN_RIGHT("40300","无权限"),
    UN_EXIST("40400","资源不存在"),
    CLOUDE_ERROR("50000","服务器内部错误"),
    ;

    private String code;
    private String info;

    ResponseCode(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
