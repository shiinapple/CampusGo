package site.shiinapple.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import site.shiinapple.api.dto.OrderDTO;
import site.shiinapple.api.dto.OrderPageResponse;
import site.shiinapple.domain.order.model.valobj.OrderVO;
import site.shiinapple.domain.order.service.IOrderService;
import site.shiinapple.types.model.Result;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单控制器
 */
@Slf4j
@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String AUTH_TOKEN_PREFIX = "campusgo:auth:token:";

    /**
     * 获取订单列表
     */
    @GetMapping("")
    public Result<OrderPageResponse> queryOrderList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "sort", required = false) String sort) {
        try {
            List<OrderVO> orderVOs = orderService.queryOrderList(type, sort, page, limit);
            Long total = orderService.queryOrderCount(type);

            List<OrderDTO> list = orderVOs.stream().map(this::toDTO).collect(Collectors.toList());

            return Result.success(OrderPageResponse.builder()
                    .list(list)
                    .total(total)
                    .build());
        } catch (Exception e) {
            log.error("查询订单列表失败", e);
            return Result.fail(40001, "查询失败: " + e.getMessage());
        }
    }

    /**
     * 抢单
     */
    @PostMapping("/{id}/grab")
    public Result<OrderDTO> grabOrder(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("id") String orderId) {
        try {
            // 1. 权限校验
            String redisKey = AUTH_TOKEN_PREFIX + token;
            String userId = redisTemplate.opsForValue().get(redisKey);
            if (!StringUtils.hasText(userId)) {
                return Result.unLogin();
            }

            // 2. 执行抢单
            OrderVO orderVO = orderService.grabOrder(orderId, userId);

            return Result.success(toDTO(orderVO));
        } catch (Exception e) {
            log.error("抢单失败, orderId: {}", orderId, e);
            return Result.fail(40001, "抢单失败: " + e.getMessage());
        }
    }

    private OrderDTO toDTO(OrderVO vo) {
        return OrderDTO.builder()
                .id(vo.getOrderId())
                .type(vo.getType())
                .status(vo.getStatus())
                .reward(String.valueOf(vo.getReward()))
                .timeLimit(vo.getTimeLimit())
                .pickupLocation(vo.getPickupLocation())
                .dropoffLocation(vo.getDropoffLocation())
                .remarks(vo.getRemarks())
                .build();
    }
}
