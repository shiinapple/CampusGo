package site.shiinapple.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 订单分页响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderPageResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单列表 */
    private List<OrderDTO> list;

    /** 总条数 */
    private Long total;

}
