package site.shiinapple.domain.address.service;

import site.shiinapple.domain.address.model.valobj.AddressVO;

import java.util.List;

/**
 * 地址服务接口
 */
public interface IAddressService {

    /** 获取用户地址列表 */
    List<AddressVO> queryAddressList(String userId);

    /** 新增地址 */
    AddressVO addAddress(String userId, String name, String detail, boolean isDefault);

    /** 更新地址 */
    AddressVO updateAddress(String userId, String addressId, String name, String detail, boolean isDefault);

    /** 删除地址 */
    void deleteAddress(String userId, String addressId);

    /** 设置默认地址 */
    void setDefaultAddress(String userId, String addressId);

}
