package site.shiinapple.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.alibaba.fastjson2.JSONObject;
import site.shiinapple.api.dto.GetResponse;
import site.shiinapple.api.dto.LoginRequest;
import site.shiinapple.api.dto.LoginResponse;
import site.shiinapple.api.dto.UserDTO;
import site.shiinapple.domain.user.model.valobj.UserVO;
import site.shiinapple.domain.user.service.IUserService;
import site.shiinapple.types.model.Result;

import java.util.UUID; // 如果你有真实的Token生成工具类，替换掉这个
import java.util.concurrent.TimeUnit;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/v1/auth")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String AUTH_TOKEN_PREFIX = "campusgo:auth:token:";

    @Value("${wx.miniapp.appid:}")
    private String wxAppId;
    @Value("${wx.miniapp.secret:}")
    private String wxSecret;

    /**
     * 用户登录/注册接口
     * @param request 包含微信 code
     * @return Result<LoginResponse> 包含 token 和用户信息
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        String code = request.getCode();

        try {
            if (!StringUtils.hasText(code)) {
                return Result.fail(40001, "code不能为空");
            }
            if (!StringUtils.hasText(wxAppId) || !StringUtils.hasText(wxSecret)) {
                return Result.fail(40001, "微信配置缺失");
            }
            String url = UriComponentsBuilder.fromHttpUrl("https://api.weixin.qq.com/sns/jscode2session")
                    .queryParam("appid", wxAppId)
                    .queryParam("secret", wxSecret)
                    .queryParam("js_code", code)
                    .queryParam("grant_type", "authorization_code")
                    .toUriString();
            String body = new RestTemplate().getForObject(url, String.class);
            JSONObject json = JSONObject.parseObject(body);
            String openId = json == null ? null : json.getString("openid");
            String unionId = json == null ? null : json.getString("unionid");
            String sessionKey = json == null ? null : json.getString("session_key");
            Integer errCode = json == null ? null : json.getInteger("errcode");
            String errMsg = json == null ? null : json.getString("errmsg");
            if (errCode != null && errCode != 0) {
                return Result.fail(40001, "微信登录失败:" + errMsg);
            }
            if (!StringUtils.hasText(openId)) {
                return Result.fail(40001, "微信登录失败: openid为空");
            }
            log.info("微信登录换取成功, openId:{}, unionId:{}, sessionKeyExists:{}", openId, unionId, StringUtils.hasText(sessionKey));

            UserVO userVO = userService.login(openId);

            UserDTO userDTO = UserDTO.builder()
                    .userId(userVO.getUserId())
                    .displayName(userVO.getDisplayName())
                    .avatarUrl(userVO.getAvatarUrl())
                    .phone(userVO.getPhone())
                    .wechatId(userVO.getWechatId())
                    .verified(userVO.isVerified())
                    .build();

            String token = "jwt_" + UUID.randomUUID().toString().replace("-", "");

            // redis存储UUID
            redisTemplate.opsForValue().set(AUTH_TOKEN_PREFIX + token, userVO.getUserId(), 1, TimeUnit.HOURS);

            LoginResponse response = LoginResponse.builder()
                    .token(token)
                    .user(userDTO)
                    .build();

            return Result.success(response);
        } catch (Exception e) {
            log.error("用户登录失败, code: {}", code, e);
            return Result.fail(40001, "登录失败: " + e.getMessage());
        }
    }


    /**
     * 获得用户信息
     * @param token
     * @return
     */
    @GetMapping("/user")
    public Result<GetResponse> get(@RequestHeader(value = "Authorization", required = false) String token/* TODO: 前端需要返回token */){
        /**
         * Redis 记录(token, UserId) 这对键值对
         * 前端返回 token 后 Redis 会提供对应的UserId
         */
        String redisKey = AUTH_TOKEN_PREFIX + token;
        log.info("🔍 正在从 Redis 查询 Key: [{}]", redisKey); // 这一行能救命，让你看清到底查的是啥
        String userId = redisTemplate.opsForValue().get(redisKey);        if (token == null) {
            // TODO：前端应该有默认用户设置
            return Result.unLogin();
        }

        UserVO userVO = userService.get(userId);

        System.out.println("3. [Controller层] 准备转换的 VO 名字: " + userVO.getDisplayName());

        UserDTO userDTO = UserDTO.builder()
                .userId(userVO.getUserId())
                .displayName(userVO.getDisplayName())
                .avatarUrl(userVO.getAvatarUrl())
                .phone(userVO.getPhone())
                .wechatId(userVO.getWechatId())
                .verified(userVO.isVerified())
                .build();

        GetResponse response = GetResponse.builder()
                .userDTO(userDTO)
                .build();

        return Result.success(response);
    }

}
