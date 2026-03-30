package site.shiinapple.api.dto;

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
    private String id;

    /** 订单类型：快递 | 外卖 */
    private String type;

    /** 订单状态：new | grabbed | completed */
    private String status;

    /** 赏金 */
    private String reward;

    /** 时间限制 */
    private String timeLimit;

    /** 取货地点 */
    private String pickupLocation;

    /** 送货地点 */
    private String dropoffLocation;

    /** 备注 */
    private String remarks;

}
