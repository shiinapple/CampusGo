package site.shiinapple.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 地址保存请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 地址名称 */
    private String name;

    /** 详细地址 */
    private String detail;

    /** 是否默认 */
    @JsonProperty("isDefault")
    private boolean isDefault;

}
