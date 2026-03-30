package site.shiinapple.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import site.shiinapple.infrastructure.dao.po.OrderPO;

import java.util.List;

/**
 * 订单 DAO 接口
 */
@Mapper
public interface IOrderDao {

    /** 插入订单 */
    void insert(OrderPO orderPO);

    /** 更新订单 */
    void update(OrderPO orderPO);

    /** 根据 ID 查询 */
    OrderPO queryOrderById(@Param("orderId") String orderId);

    /** 分页查询列表 */
    List<OrderPO> queryOrderList(@Param("type") String type, @Param("sort") String sort, @Param("offset") int offset, @Param("limit") int limit);

    /** 查询总数 */
    Long queryOrderCount(@Param("type") String type);

}
