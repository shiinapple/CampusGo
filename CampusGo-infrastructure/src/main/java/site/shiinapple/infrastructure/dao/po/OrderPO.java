package site.shiinapple.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 订单持久化对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderPO {

    private Long id;

    private String orderId;

    private String type;

    private String status;

    private Double reward;

    private String timeLimit;

    private String pickupLocation;

    private String dropoffLocation;

    private String remarks;

    private String creatorUserId;

    private String takerUserId;

    private Date createTime;

    private Date updateTime;

}
