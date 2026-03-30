package site.shiinapple.domain.address.adapter.repository;

import site.shiinapple.domain.address.model.aggregate.Address;

import java.util.List;

/**
 * 地址仓储接口
 */
public interface IAddressRepository {

    List<Address> queryAddressListByUserId(String userId);

    Address queryAddressByAddressId(String userId, String addressId);

    void save(Address address);

    void delete(String userId, String addressId);

    void clearDefault(String userId);

}
