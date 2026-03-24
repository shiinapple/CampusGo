package site.shiinapple.domain.user.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import site.shiinapple.domain.user.model.valobj.UserVO;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String userId;

    private String openId;

    private String displayName;

    private String wechatId;

    private String phone;

    private String avatarUrl;

    private boolean verified;

    /**
     * 创建新用户
     * @param openId 微信OpenID
     * @return User 聚合根
     */
    public static User create(String openId) {
        if (StringUtils.isBlank(openId)) {
            throw new IllegalArgumentException("openId cannot be blank");
        }
        return User.builder()
                .userId(UUID.randomUUID().toString().replace("-", ""))
                .openId(openId)
                .displayName("新用户")
                .verified(false)
                .build();
    }

    /**
     * 更新基础资料
     */
    public void updateProfile(String displayName, String avatarUrl) {
        if (StringUtils.isNotBlank(displayName)) {
            this.displayName = displayName;
        }
        if (StringUtils.isNotBlank(avatarUrl)) {
            this.avatarUrl = avatarUrl;
        }
    }

    /**
     * 绑定手机号
     */
    public void bindPhone(String phone) {
        if (StringUtils.isNotBlank(phone)) {
            this.phone = phone;
        }
    }

    /**
     * 设置实名状态
     */
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
