package site.shiinapple.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 地址持久化对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressPO {

    private Long id;

    private String addressId;

    private String userId;

    private String name;

    private String detail;

    private Integer isDefault;

    private Date createTime;

    private Date updateTime;

}
