package site.shiinapple.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import site.shiinapple.api.dto.AddressDTO;
import site.shiinapple.api.dto.AddressRequest;
import site.shiinapple.domain.address.model.valobj.AddressVO;
import site.shiinapple.domain.address.service.IAddressService;
import site.shiinapple.types.model.Result;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 地址控制器
 */
@Slf4j
@RestController
@RequestMapping("/v1/addresses")
public class AddressController {

    @Autowired
    private IAddressService addressService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String AUTH_TOKEN_PREFIX = "campusgo:auth:token:";

    /**
     * 获取地址列表
     */
    @GetMapping("")
    public Result<List<AddressDTO>> queryAddressList(@RequestHeader(value = "Authorization") String token) {
        try {
            String userId = getUserId(token);
            if (userId == null) return Result.unLogin();

            List<AddressVO> vos = addressService.queryAddressList(userId);
            List<AddressDTO> dtos = vos.stream().map(this::toDTO).collect(Collectors.toList());
            return Result.success(dtos);
        } catch (Exception e) {
            log.error("获取地址列表失败", e);
            return Result.fail(40001, "查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增地址
     */
    @PostMapping("")
    public Result<AddressDTO> addAddress(@RequestHeader(value = "Authorization") String token, @RequestBody AddressRequest request) {
        try {
            String userId = getUserId(token);
            if (userId == null) return Result.unLogin();

            AddressVO vo = addressService.addAddress(userId, request.getName(), request.getDetail(), request.isDefault());
            return Result.success(toDTO(vo));
        } catch (Exception e) {
            log.error("新增地址失败", e);
            return Result.fail(40001, "保存失败: " + e.getMessage());
        }
    }

    /**
     * 更新地址
     */
    @PutMapping("/{id}")
    public Result<AddressDTO> updateAddress(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable("id") String addressId,
            @RequestBody AddressRequest request) {
        try {
            String userId = getUserId(token);
            if (userId == null) return Result.unLogin();

            AddressVO vo = addressService.updateAddress(userId, addressId, request.getName(), request.getDetail(), request.isDefault());
            return Result.success(toDTO(vo));
        } catch (Exception e) {
            log.error("更新地址失败", e);
            return Result.fail(40001, "更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除地址
     */
    @DeleteMapping("/{id}")
    public Result<Object> deleteAddress(@RequestHeader(value = "Authorization") String token, @PathVariable("id") String addressId) {
        try {
            String userId = getUserId(token);
            if (userId == null) return Result.unLogin();

            addressService.deleteAddress(userId, addressId);
            return Result.success(new Object());
        } catch (Exception e) {
            log.error("删除地址失败", e);
            return Result.fail(40001, "删除失败: " + e.getMessage());
        }
    }

    /**
     * 设置默认地址
     */
    @PutMapping("/{id}/default")
    public Result<Object> setDefaultAddress(@RequestHeader(value = "Authorization") String token, @PathVariable("id") String addressId) {
        try {
            String userId = getUserId(token);
            if (userId == null) return Result.unLogin();

            addressService.setDefaultAddress(userId, addressId);
            return Result.success(new Object());
        } catch (Exception e) {
            log.error("设置默认地址失败", e);
            return Result.fail(40001, "设置失败: " + e.getMessage());
        }
    }

    private String getUserId(String token) {
        String redisKey = AUTH_TOKEN_PREFIX + token;
        return redisTemplate.opsForValue().get(redisKey);
    }

    private AddressDTO toDTO(AddressVO vo) {
        return AddressDTO.builder()
                .addressId(vo.getAddressId())
                .name(vo.getName())
                .detail(vo.getDetail())
                .isDefault(vo.isDefault())
                .build();
    }
}
