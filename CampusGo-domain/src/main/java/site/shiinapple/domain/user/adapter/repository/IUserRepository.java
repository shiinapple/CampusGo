package site.shiinapple.domain.user.adapter.repository;

import site.shiinapple.domain.user.model.aggregate.User;

public interface IUserRepository {

    User findByOpenId(String openId);

    void save(User user);

    User queryUserByUserId(String userId);

}
