package site.shiinapple.domain.user.service;

import site.shiinapple.domain.user.model.valobj.UserVO;

public interface IUserService {

    /**
     * 用户登录/注册
     * @param code 微信 code 或 openId
     * @return UserVO
     */
    UserVO login(String code);

    /**
     * 根据用户 ID 获取用户信息
     * @param userId 用户 ID
     * @return UserVO
     */
    UserVO get(String userId);

    /**
     * 更新用户信息
     * @param userId 用户 ID
     * @param userVO 更新的信息
     * @return 更新后的 UserVO
     */
    UserVO update(String userId, UserVO userVO);

}
