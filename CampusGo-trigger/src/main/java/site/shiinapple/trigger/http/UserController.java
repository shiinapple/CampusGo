package site.shiinapple.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.multipart.MultipartFile;
import site.shiinapple.api.dto.GetResponse;
import site.shiinapple.api.dto.LoginRequest;
import site.shiinapple.api.dto.LoginResponse;
import site.shiinapple.api.dto.UserDTO;
import site.shiinapple.api.dto.UserUpdateRequest;
import site.shiinapple.domain.user.model.valobj.UserVO;
import site.shiinapple.domain.user.service.IUserService;
import site.shiinapple.types.model.Result;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID; // 如果你有真实的Token生成工具类，替换掉这个
import java.util.concurrent.TimeUnit;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/v1")
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

    @Value("${upload.path:./data/upload/}")
    private String uploadPath;

    @Value("${upload.baseUrl:http://49.233.32.212:8091/api/upload/}")
    private String uploadBaseUrl;

    /**
     * 用户登录/注册接口
     * @param request 包含微信 code
     * @return Result<LoginResponse> 包含 token 和用户信息
     */
    @PostMapping("/auth/login")
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
                    .monthTaken(userVO.getMonthTaken())
                    .totalTaken(userVO.getTotalTaken())
                    .onTimeRate(userVO.getOnTimeRate())
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
     * @param token 授权令牌
     * @return Result<UserDTO> 用户信息
     */
    @GetMapping("/auth/user")
    public Result<UserDTO> get(@RequestHeader(value = "Authorization") String token) {
        try {
            // 1. 获取当前用户 ID
            String redisKey = AUTH_TOKEN_PREFIX + token;
            String userId = redisTemplate.opsForValue().get(redisKey);
            if (!StringUtils.hasText(userId)) {
                return Result.unLogin();
            }

            // 2. 查询用户信息
            UserVO userVO = userService.get(userId);
            if (userVO == null) {
                return Result.fail(40001, "用户不存在");
            }

            // 3. 转换并返回
            UserDTO userDTO = UserDTO.builder()
                    .userId(userVO.getUserId())
                    .displayName(userVO.getDisplayName())
                    .avatarUrl(userVO.getAvatarUrl())
                    .phone(userVO.getPhone())
                    .wechatId(userVO.getWechatId())
                    .verified(userVO.isVerified())
                    .monthTaken(userVO.getMonthTaken())
                    .totalTaken(userVO.getTotalTaken())
                    .onTimeRate(userVO.getOnTimeRate())
                    .build();

            return Result.success(userDTO);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Result.fail(40001, "获取失败: " + e.getMessage());
        }
    }

    /**
     * 更新当前登录用户信息
     * @param token 授权令牌
     * @param request 更新请求
     * @return Result<UserDTO> 更新后的用户信息
     */
    @PutMapping("/auth/user")
    public Result<UserDTO> update(@RequestHeader(value = "Authorization") String token, @RequestBody UserUpdateRequest request) {
        try {
            // 1. 获取当前用户 ID
            String redisKey = AUTH_TOKEN_PREFIX + token;
            String userId = redisTemplate.opsForValue().get(redisKey);
            if (!StringUtils.hasText(userId)) {
                return Result.unLogin();
            }

            // 2. 转换 DTO 为 VO (增加清洗逻辑)
            UserVO userVO = UserVO.builder()
                    .displayName(request.getDisplayName())
                    .avatarUrl(request.getCleanAvatarUrl()) // 使用清洗后的 URL
                    .wechatId(request.getWechatId())
                    .phone(request.getPhone())
                    .build();

            log.info("准备更新用户 VO: {}", userVO);

            // 3. 执行更新
            UserVO updatedUserVO = userService.update(userId, userVO);

            // 4. 转换结果为 DTO
            UserDTO userDTO = UserDTO.builder()
                    .userId(updatedUserVO.getUserId())
                    .displayName(updatedUserVO.getDisplayName())
                    .avatarUrl(updatedUserVO.getAvatarUrl())
                    .phone(updatedUserVO.getPhone())
                    .wechatId(updatedUserVO.getWechatId())
                    .verified(updatedUserVO.isVerified())
                    .monthTaken(updatedUserVO.getMonthTaken())
                    .totalTaken(updatedUserVO.getTotalTaken())
                    .onTimeRate(updatedUserVO.getOnTimeRate())
                    .build();

            return Result.success(userDTO);
        } catch (Exception e) {
            log.error("更新用户信息失败", e);
            return Result.fail(40001, "更新失败: " + e.getMessage());
        }
    }

    /**
     * 上传头像接口 (包含激进压缩逻辑 + 自动存库)
     * @param token 授权令牌
     * @param file 图片文件
     * @return Result<UserDTO> 返回更新后的完整用户信息
     */
    @PostMapping("/auth/upload")
    public Result<UserDTO> upload(@RequestHeader(value = "Authorization") String token, @RequestParam("file") MultipartFile file) {
        try {
            // 1. 权限校验
            String redisKey = AUTH_TOKEN_PREFIX + token;
            String userId = redisTemplate.opsForValue().get(redisKey);
            if (!StringUtils.hasText(userId)) {
                return Result.unLogin();
            }

            if (file.isEmpty()) {
                return Result.fail(40001, "文件不能为空");
            }

            // 2. 确保目录存在
            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 3. 生成文件名并压缩保存 (保持之前的激进压缩逻辑)
            String fileName = UUID.randomUUID().toString().replace("-", "") + ".jpg";
            File destFile = new File(dir, fileName);
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            
            int targetWidth = 200;
            int targetHeight = 200;
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            if (width > height) {
                targetHeight = (int) (height * (targetWidth / (double) width));
            } else {
                targetWidth = (int) (width * (targetHeight / (double) height));
            }

            BufferedImage thumbnail = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = thumbnail.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
            g2d.dispose();

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            if (!writers.hasNext()) throw new RuntimeException("No writers for jpg");
            ImageWriter writer = writers.next();
            try (FileOutputStream fos = new FileOutputStream(destFile);
                 ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {
                writer.setOutput(ios);
                ImageWriteParam param = writer.getDefaultWriteParam();
                if (param.canWriteCompressed()) {
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(0.6f);
                }
                writer.write(null, new IIOImage(thumbnail, null, null), param);
            } finally {
                writer.dispose();
            }

            // 4. 【核心变动】自动更新数据库
            String newAvatarUrl = uploadBaseUrl + fileName;
            UserVO updateVO = UserVO.builder()
                    .avatarUrl(newAvatarUrl)
                    .build();
            
            // 执行 Service 层存库
            UserVO updatedUserVO = userService.update(userId, updateVO);
            log.info("用户 {} 头像上传并自动存库成功: {}", userId, newAvatarUrl);

            // 5. 直接返回更新后的完整用户对象
            UserDTO userDTO = UserDTO.builder()
                    .userId(updatedUserVO.getUserId())
                    .displayName(updatedUserVO.getDisplayName())
                    .avatarUrl(updatedUserVO.getAvatarUrl())
                    .phone(updatedUserVO.getPhone())
                    .wechatId(updatedUserVO.getWechatId())
                    .verified(updatedUserVO.isVerified())
                    .monthTaken(updatedUserVO.getMonthTaken())
                    .totalTaken(updatedUserVO.getTotalTaken())
                    .onTimeRate(updatedUserVO.getOnTimeRate())
                    .build();

            return Result.success(userDTO);
        } catch (Exception e) {
            log.error("上传并更新头像失败", e);
            return Result.fail(40001, "上传失败: " + e.getMessage());
        }
    }

    /**
     * 校园认证接口
     */
    @PostMapping("/verify/submit")
    public Result<Map<String, Object>> verifySubmit(@RequestHeader(value = "Authorization") String token, @RequestParam("file") MultipartFile file) {
        try {
            // 1. 权限校验
            String redisKey = AUTH_TOKEN_PREFIX + token;
            String userId = redisTemplate.opsForValue().get(redisKey);
            if (!StringUtils.hasText(userId)) {
                return Result.unLogin();
            }

            // 2. 模拟保存证件逻辑 (这里可以复用之前的图片压缩逻辑，暂简化为自动通过)
            UserVO userVO = UserVO.builder().verified(true).build();
            userService.update(userId, userVO);

            Map<String, Object> result = new HashMap<>();
            result.put("verified", true);
            result.put("status", "approved");
            return Result.success(result);
        } catch (Exception e) {
            log.error("校园认证提交失败", e);
            return Result.fail(40001, "提交失败: " + e.getMessage());
        }
    }
}
