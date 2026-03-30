package site.shiinapple.domain.order.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 订单聚合根
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

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

    /** 发单人 ID */
    private String creatorUserId;

    /** 抢单人 ID */
    private String takerUserId;

    /**
     * 抢单操作
     * @param userId 抢单人 ID
     * @return boolean 是否抢单成功
     */
    public boolean grab(String userId) {
        if (!"new".equals(this.status)) {
            return false;
        }
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        this.takerUserId = userId;
        this.status = "grabbed";
        return true;
    }

}
