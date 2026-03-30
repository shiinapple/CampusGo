package site.shiinapple.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import site.shiinapple.infrastructure.dao.po.UserPO;

@Mapper
public interface IUserDao {

    UserPO queryUserByOpenId(String openId);

    void insert(UserPO userPO);

    void update(UserPO userPO);

    UserPO queryUserByUserId(String userId);

}
