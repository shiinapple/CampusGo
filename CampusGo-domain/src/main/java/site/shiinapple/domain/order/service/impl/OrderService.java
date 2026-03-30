package site.shiinapple.domain.order.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.shiinapple.domain.order.adapter.repository.IOrderRepository;
import site.shiinapple.domain.order.model.aggregate.Order;
import site.shiinapple.domain.order.model.valobj.OrderVO;
import site.shiinapple.domain.order.service.IOrderService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Slf4j
@Service
public class OrderService implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public List<OrderVO> queryOrderList(String type, String sort, int page, int limit) {
        int offset = (page - 1) * limit;
        List<Order> orders = orderRepository.queryOrderList(type, sort, offset, limit);
        return orders.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public Long queryOrderCount(String type) {
        return orderRepository.queryOrderCount(type);
    }

    @Override
    public OrderVO grabOrder(String orderId, String userId) {
        // 1. 获取订单
        Order order = orderRepository.queryOrderById(orderId);
        if (order == null) {
            log.error("订单不存在: {}", orderId);
            throw new RuntimeException("订单不存在");
        }

        // 2. 抢单逻辑
        boolean success = order.grab(userId);
        if (!success) {
            log.error("抢单失败: 订单已被抢走或状态错误, orderId: {}, userId: {}", orderId, userId);
            throw new RuntimeException("抢单失败，该订单已被他人抢走");
        }

        // 3. 保存更新
        orderRepository.save(order);

        // 4. 返回最新状态
        return toVO(order);
    }

    private OrderVO toVO(Order order) {
        return OrderVO.builder()
                .orderId(order.getOrderId())
                .type(order.getType())
                .status(order.getStatus())
                .reward(order.getReward())
                .timeLimit(order.getTimeLimit())
                .pickupLocation(order.getPickupLocation())
                .dropoffLocation(order.getDropoffLocation())
                .remarks(order.getRemarks())
                .takerUserId(order.getTakerUserId())
                .build();
    }
}
