package site.shiinapple.domain.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
        UserVO userVO = new UserVO();

        User user = userRepository.queryUserByUserId(userId);
        System.out.println("2. [Service层] 聚合根里的名字: " + user.getDisplayName());

        return UserVO.builder()
                .userId(user.getUserId())
                .openId(user.getOpenId())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .phone(user.getPhone())
                .wechatId(user.getWechatId())
                .verified(user.isVerified()) // boolean 类型的 getter 是 isXxx()
                .build();
    }

}
