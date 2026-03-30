package site.shiinapple.domain.address.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 地址值对象
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 地址 ID */
    private String addressId;

    /** 地址名称 */
    private String name;

    /** 详细地址 */
    private String detail;

    /** 是否默认 */
    private boolean isDefault;

}
