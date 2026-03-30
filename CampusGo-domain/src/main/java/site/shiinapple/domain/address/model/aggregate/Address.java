package site.shiinapple.domain.address.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * 地址聚合根
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    /** 地址唯一标识 */
    private String addressId;

    /** 用户 ID */
    private String userId;

    /** 地址名称 */
    private String name;

    /** 详细地址 */
    private String detail;

    /** 是否默认 */
    private boolean isDefault;

    /**
     * 创建新地址
     */
    public static Address create(String userId, String name, String detail, boolean isDefault) {
        return Address.builder()
                .addressId("addr_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8))
                .userId(userId)
                .name(name)
                .detail(detail)
                .isDefault(isDefault)
                .build();
    }

    /**
     * 更新地址信息
     */
    public void update(String name, String detail, boolean isDefault) {
        if (StringUtils.isNotBlank(name)) {
            this.name = name;
        }
        if (StringUtils.isNotBlank(detail)) {
            this.detail = detail;
        }
        this.isDefault = isDefault;
    }

    /**
     * 设置为默认地址
     */
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

}
