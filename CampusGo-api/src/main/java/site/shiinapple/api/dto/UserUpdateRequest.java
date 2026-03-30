package site.shiinapple.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户信息更新请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新昵称
     */
    @JsonProperty("displayName")
    private String displayName;

    /**
     * 微信号
     */
    @JsonProperty("wechatId")
    private String wechatId;

    /**
     * 手机号
     */
    @JsonProperty("phone")
    private String phone;

    /**
     * 头像 URL
     */
    @JsonProperty("avatarUrl")
    private String avatarUrl;

    /**
     * 性别
     */
    @JsonProperty("gender")
    private Integer gender;

    /**
     * 清洗 URL 中的杂质（如反引号、空格等）
     */
    public String getCleanAvatarUrl() {
        if (avatarUrl == null) return null;
        // 同时清理反引号、引号、空格和换行
        return avatarUrl.trim().replace("`", "").replace("\"", "").replace(" ", "").replace("\n", "").replace("\r", "");
    }

}
