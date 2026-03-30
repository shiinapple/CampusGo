package site.shiinapple.infrastructure.adapter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import site.shiinapple.domain.order.adapter.repository.IOrderRepository;
import site.shiinapple.domain.order.model.aggregate.Order;
import site.shiinapple.infrastructure.dao.IOrderDao;
import site.shiinapple.infrastructure.dao.po.OrderPO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单仓储实现类
 */
@Repository
public class OrderRepository implements IOrderRepository {

    @Autowired
    private IOrderDao orderDao;

    @Override
    public List<Order> queryOrderList(String type, String sort, int offset, int limit) {
        List<OrderPO> pos = orderDao.queryOrderList(type, sort, offset, limit);
        return pos.stream().map(this::toAggregate).collect(Collectors.toList());
    }

    @Override
    public Long queryOrderCount(String type) {
        return orderDao.queryOrderCount(type);
    }

    @Override
    public Order queryOrderById(String orderId) {
        OrderPO po = orderDao.queryOrderById(orderId);
        return po == null ? null : toAggregate(po);
    }

    @Override
    public void save(Order order) {
        OrderPO po = orderDao.queryOrderById(order.getOrderId());
        if (po == null) {
            po = OrderPO.builder()
                    .orderId(order.getOrderId())
                    .type(order.getType())
                    .status(order.getStatus())
                    .reward(order.getReward())
                    .timeLimit(order.getTimeLimit())
                    .pickupLocation(order.getPickupLocation())
                    .dropoffLocation(order.getDropoffLocation())
                    .remarks(order.getRemarks())
                    .creatorUserId(order.getCreatorUserId())
                    .takerUserId(order.getTakerUserId())
                    .build();
            orderDao.insert(po);
        } else {
            po.setStatus(order.getStatus());
            po.setTakerUserId(order.getTakerUserId());
            orderDao.update(po);
        }
    }

    private Order toAggregate(OrderPO po) {
        return Order.builder()
                .orderId(po.getOrderId())
                .type(po.getType())
                .status(po.getStatus())
                .reward(po.getReward())
                .timeLimit(po.getTimeLimit())
                .pickupLocation(po.getPickupLocation())
                .dropoffLocation(po.getDropoffLocation())
                .remarks(po.getRemarks())
                .creatorUserId(po.getCreatorUserId())
                .takerUserId(po.getTakerUserId())
                .build();
    }
}
