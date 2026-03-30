package site.shiinapple.domain.order.service;

import site.shiinapple.domain.order.model.valobj.OrderVO;

import java.util.List;

/**
 * 订单服务接口
 */
public interface IOrderService {

    /**
     * 查询订单列表（带筛选和排序）
     * @param type 订单类型
     * @param sort 排序字段
     * @param page 页码
     * @param limit 每页条数
     * @return 订单列表
     */
    List<OrderVO> queryOrderList(String type, String sort, int page, int limit);

    /**
     * 查询订单总数
     * @param type 订单类型
     * @return 总数
     */
    Long queryOrderCount(String type);

    /**
     * 抢单
     * @param orderId 订单 ID
     * @param userId 抢单人 ID
     * @return 更新后的 OrderVO
     */
    OrderVO grabOrder(String orderId, String userId);

}
