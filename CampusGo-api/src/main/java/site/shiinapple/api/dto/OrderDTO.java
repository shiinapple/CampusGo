package site.shiinapple.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单信息 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单 ID */
    @JsonProperty("id")
    private String id;

    /** 订单类型：快递 | 外卖 */
    @JsonProperty("type")
    private String type;

    /** 订单状态：new | grabbed | completed */
    @JsonProperty("status")
    private String status;

    /** 赏金 */
    @JsonProperty("reward")
    private String reward;

    /** 时间限制 */
    @JsonProperty("timeLimit")
    private String timeLimit;

    /** 取货地点 */
    @JsonProperty("pickupLocation")
    private String pickupLocation;

    /** 送货地点 */
    @JsonProperty("dropoffLocation")
    private String dropoffLocation;

    /** 备注 */
    @JsonProperty("remarks")
    private String remarks;

}
