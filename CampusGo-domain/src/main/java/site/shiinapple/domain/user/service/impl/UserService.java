package site.shiinapple.domain.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.shiinapple.domain.user.adapter.repository.IUserRepository;
import site.shiinapple.domain.user.model.aggregate.User;
import site.shiinapple.domain.user.model.valobj.UserVO;
import site.shiinapple.domain.user.service.IUserService;

@Slf4j
@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserVO login(String code) {
        // 1. 查询用户
        User user = userRepository.findByOpenId(code);

        // 2. 如果用户不存在，则创建并保存
        if (user == null) {
            log.info("用户不存在，开始创建新用户: {}", code);
            user = User.create(code);
            userRepository.save(user);
        }

        // 3. 返回 UserVO
        return UserVO.builder()
                .userId(user.getUserId())
                .openId(user.getOpenId())
                .displayName(user.getDisplayName())
                .wechatId(user.getWechatId())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .verified(user.isVerified())
                .build();
    }

    @Override
    public UserVO get(String userId) {
        User user = userRepository.queryUserByUserId(userId);
        if (user == null) {
            return null;
        }
        return UserVO.builder()
                .userId(user.getUserId())
                .openId(user.getOpenId())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .phone(user.getPhone())
                .wechatId(user.getWechatId())
                .verified(user.isVerified())
                .build();
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
        return UserVO.builder()
                .userId(user.getUserId())
                .openId(user.getOpenId())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .phone(user.getPhone())
                .wechatId(user.getWechatId())
                .verified(user.isVerified())
                .build();
    }

}
