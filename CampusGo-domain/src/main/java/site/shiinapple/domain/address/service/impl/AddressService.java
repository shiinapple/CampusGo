package site.shiinapple.domain.address.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.shiinapple.domain.address.adapter.repository.IAddressRepository;
import site.shiinapple.domain.address.model.aggregate.Address;
import site.shiinapple.domain.address.model.valobj.AddressVO;
import site.shiinapple.domain.address.service.IAddressService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 地址服务实现类
 */
@Slf4j
@Service
public class AddressService implements IAddressService {

    @Autowired
    private IAddressRepository addressRepository;

    @Override
    public List<AddressVO> queryAddressList(String userId) {
        List<Address> addresses = addressRepository.queryAddressListByUserId(userId);
        return addresses.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddressVO addAddress(String userId, String name, String detail, boolean isDefault) {
        if (isDefault) {
            addressRepository.clearDefault(userId);
        }
        Address address = Address.create(userId, name, detail, isDefault);
        addressRepository.save(address);
        return toVO(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddressVO updateAddress(String userId, String addressId, String name, String detail, boolean isDefault) {
        Address address = addressRepository.queryAddressByAddressId(userId, addressId);
        if (address == null) {
            throw new RuntimeException("地址不存在");
        }
        if (isDefault) {
            addressRepository.clearDefault(userId);
        }
        address.update(name, detail, isDefault);
        addressRepository.save(address);
        return toVO(address);
    }

    @Override
    public void deleteAddress(String userId, String addressId) {
        addressRepository.delete(userId, addressId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultAddress(String userId, String addressId) {
        Address address = addressRepository.queryAddressByAddressId(userId, addressId);
        if (address == null) {
            throw new RuntimeException("地址不存在");
        }
        addressRepository.clearDefault(userId);
        address.setDefault(true);
        addressRepository.save(address);
    }

    private AddressVO toVO(Address address) {
        return AddressVO.builder()
                .addressId(address.getAddressId())
                .name(address.getName())
                .detail(address.getDetail())
                .isDefault(address.isDefault())
                .build();
    }
}
