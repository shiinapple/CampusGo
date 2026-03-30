package site.shiinapple.domain.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.shiinapple.domain.order.adapter.repository.IOrderRepository;
import site.shiinapple.domain.user.adapter.repository.IUserRepository;
import site.shiinapple.domain.user.model.aggregate.User;
import site.shiinapple.domain.user.model.valobj.UserVO;
import site.shiinapple.domain.user.service.IUserService;

@Slf4j
@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public UserVO login(String openId) {
        // 1. 查询用户
        User user = userRepository.findByOpenId(openId);

        // 2. 如果用户不存在，则创建并保存
        if (user == null) {
            log.info("用户不存在，开始创建新用户: {}", openId);
            user = User.create(openId);
            userRepository.save(user);
        }

        // 3. 返回 UserVO
        return toVO(user);
    }

    @Override
    public UserVO get(String userId) {
        User user = userRepository.queryUserByUserId(userId);
        if (user == null) {
            return null;
        }
        return toVO(user);
    }

    @Override
    public UserVO update(String userId, UserVO userVO) {
        // 1. 获取现有用户信息
        User user = userRepository.queryUserByUserId(userId);
        if (user == null) {
            log.error("用户不存在, userId: {}", userId);
            throw new RuntimeException("用户不存在");
        }

        // 2. 更新资料
        user.updateProfile(userVO.getDisplayName(), userVO.getAvatarUrl(), userVO.getWechatId(), userVO.getPhone());

        // 3. 保存更新后的用户
        userRepository.save(user);

        // 4. 返回更新后的 UserVO
        return toVO(user);
    }

    private UserVO toVO(User user) {
        Integer totalTaken = orderRepository.queryTotalTaken(user.getUserId());
        Integer monthTaken = orderRepository.queryMonthTaken(user.getUserId());
        Integer deliveredCount = orderRepository.queryDeliveredCount(user.getUserId());

        int onTimeRate = 0;
        if (totalTaken != null && totalTaken > 0) {
            onTimeRate = (int) Math.round((deliveredCount * 100.0) / totalTaken);
        }

        return UserVO.builder()
                .userId(user.getUserId())
                .openId(user.getOpenId())
                .displayName(user.getDisplayName())
                .wechatId(user.getWechatId())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .verified(user.isVerified())
                .totalTaken(totalTaken)
                .monthTaken(monthTaken)
                .onTimeRate(onTimeRate + "%")
                .build();
    }

}
