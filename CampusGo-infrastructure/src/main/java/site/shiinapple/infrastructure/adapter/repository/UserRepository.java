package site.shiinapple.infrastructure.adapter.repository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import site.shiinapple.domain.user.adapter.repository.IUserRepository;
import site.shiinapple.domain.user.model.aggregate.User;
import site.shiinapple.infrastructure.dao.IUserDao;
import site.shiinapple.infrastructure.dao.po.UserPO;

@Repository
public class UserRepository implements IUserRepository {

    @Autowired
    private IUserDao userDao;

    @Override
    public User findByOpenId(String openId) {
        UserPO userPO = userDao.queryUserByOpenId(openId);
        if (userPO == null) {
            return null;
        }
        return User.builder()
                .userId(userPO.getUserId())
                .openId(userPO.getOpenId())
                .displayName(userPO.getDisplayName())
                .wechatId(userPO.getWechatId())
                .phone(userPO.getPhone())
                .avatarUrl(userPO.getAvatarUrl())
                .verified(userPO.getVerified() == 1)
                .build();
    }

    @Override
    public void save(User user) {
        UserPO userPO = userDao.queryUserByUserId(user.getUserId());
        if (userPO == null) {
            userPO = UserPO.builder()
                    .userId(user.getUserId())
                    .openId(user.getOpenId())
                    .displayName(user.getDisplayName())
                    .wechatId(user.getWechatId())
                    .phone(user.getPhone())
                    .avatarUrl(user.getAvatarUrl())
                    .verified(user.isVerified() ? 1 : 0)
                    .build();
            userDao.insert(userPO);
        } else {
            userPO.setDisplayName(user.getDisplayName());
            userPO.setWechatId(user.getWechatId());
            userPO.setPhone(user.getPhone());
            userPO.setAvatarUrl(user.getAvatarUrl());
            userPO.setVerified(user.isVerified() ? 1 : 0);
            userDao.update(userPO);
        }
    }

    @Override
    public User queryUserByUserId(String userId) {
        System.out.println("DEBUG: 开始查询数据库，传入的 userId 是 -> [" + userId + "]");
        UserPO userPO = userDao.queryUserByUserId(userId);
        System.out.println("1. [Repository层] 从数据库查到的 PO: " + userPO.getDisplayName());
        if (userPO == null) {
            return null;
        }

        return User.builder()
                .userId(userPO.getUserId())
                .openId(userPO.getOpenId())
                .displayName(userPO.getDisplayName())
                .avatarUrl(userPO.getAvatarUrl())
                .phone(userPO.getPhone())
                .wechatId(userPO.getWechatId())
                // 注意：如果 PO 里 verified 是 Integer(0或1)，这里要判断 == 1；如果是 boolean，直接传
                .verified(userPO.getVerified() != null && userPO.getVerified() == 1)
                .build();
    }
}
