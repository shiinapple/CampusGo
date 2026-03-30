package site.shiinapple.infrastructure.adapter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import site.shiinapple.domain.address.adapter.repository.IAddressRepository;
import site.shiinapple.domain.address.model.aggregate.Address;
import site.shiinapple.infrastructure.dao.IAddressDao;
import site.shiinapple.infrastructure.dao.po.AddressPO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 地址仓储实现类
 */
@Repository
public class AddressRepository implements IAddressRepository {

    @Autowired
    private IAddressDao addressDao;

    @Override
    public List<Address> queryAddressListByUserId(String userId) {
        List<AddressPO> pos = addressDao.queryAddressListByUserId(userId);
        return pos.stream().map(this::toAggregate).collect(Collectors.toList());
    }

    @Override
    public Address queryAddressByAddressId(String userId, String addressId) {
        AddressPO po = addressDao.queryAddressByAddressId(userId, addressId);
        return po == null ? null : toAggregate(po);
    }

    @Override
    public void save(Address address) {
        AddressPO po = addressDao.queryAddressByAddressId(address.getUserId(), address.getAddressId());
        if (po == null) {
            po = AddressPO.builder()
                    .addressId(address.getAddressId())
                    .userId(address.getUserId())
                    .name(address.getName())
                    .detail(address.getDetail())
                    .isDefault(address.isDefault() ? 1 : 0)
                    .build();
            addressDao.insert(po);
        } else {
            po.setName(address.getName());
            po.setDetail(address.getDetail());
            po.setIsDefault(address.isDefault() ? 1 : 0);
            addressDao.update(po);
        }
    }

    @Override
    public void delete(String userId, String addressId) {
        addressDao.delete(userId, addressId);
    }

    @Override
    public void clearDefault(String userId) {
        addressDao.clearDefault(userId);
    }

    private Address toAggregate(AddressPO po) {
        return Address.builder()
                .addressId(po.getAddressId())
                .userId(po.getUserId())
                .name(po.getName())
                .detail(po.getDetail())
                .isDefault(po.getIsDefault() == 1)
                .build();
    }
}
