package site.shiinapple.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPO {

    private Long id;

    private String userId;

    private String openId;

    private String displayName;

    private String wechatId;

    private String phone;

    private String avatarUrl;

    private Integer verified;

    private Date createTime;

    private Date updateTime;

}
