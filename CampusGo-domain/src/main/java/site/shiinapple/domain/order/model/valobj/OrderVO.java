package site.shiinapple.domain.order.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单值对象
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单 ID */
    private String orderId;

    /** 订单类型：快递 | 外卖 */
    private String type;

    /** 订单状态：new | grabbed | completed */
    private String status;

    /** 赏金 */
    private Double reward;

    /** 时间限制 */
    private String timeLimit;

    /** 取货地点 */
    private String pickupLocation;

    /** 送货地点 */
    private String dropoffLocation;

    /** 备注 */
    private String remarks;

    /** 抢单人 ID */
    private String takerUserId;

}
