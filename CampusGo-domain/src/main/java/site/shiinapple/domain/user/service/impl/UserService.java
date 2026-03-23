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

}
