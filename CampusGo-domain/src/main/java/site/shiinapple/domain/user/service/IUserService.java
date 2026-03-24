package site.shiinapple.domain.user.service;

import site.shiinapple.domain.user.model.valobj.UserVO;

public interface IUserService {

    /**
     * 用户登录/注册
     * @param code 微信 code 或 openId
     * @return UserVO
     */
    UserVO login(String code);

    UserVO get(String userId);

}
