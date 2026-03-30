package site.shiinapple.domain.order.adapter.repository;

import site.shiinapple.domain.order.model.aggregate.Order;

import java.util.List;

/**
 * 订单仓储接口
 */
public interface IOrderRepository {

    /**
     * 查询订单列表
     */
    List<Order> queryOrderList(String type, String sort, int offset, int limit);

    /**
     * 查询订单总数
     */
    Long queryOrderCount(String type);

    /**
     * 根据 ID 查询订单
     */
    Order queryOrderById(String orderId);

    /**
     * 保存或更新订单
     */
    void save(Order order);

    /**
     * 查询用户接单总数
     */
    Integer queryTotalTaken(String userId);

    /**
     * 查询用户本月接单数
     */
    Integer queryMonthTaken(String userId);

    /**
     * 查询用户已送达订单数
     */
    Integer queryDeliveredCount(String userId);
}
