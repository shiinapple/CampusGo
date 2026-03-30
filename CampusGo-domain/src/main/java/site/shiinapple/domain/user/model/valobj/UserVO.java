package site.shiinapple.domain.user.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * user:
 * userId displayName wechatId phone avatarUrl address status
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

    private String userId;

    private String openId;

    private String displayName;

    private String wechatId;

    private String phone;

    private String avatarUrl;

    private boolean verified;

    private Integer monthTaken;

    private Integer totalTaken;

    private String onTimeRate;

}