package site.shiinapple.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import site.shiinapple.infrastructure.dao.po.AddressPO;

import java.util.List;

/**
 * 地址 DAO 接口
 */
@Mapper
public interface IAddressDao {

    void insert(AddressPO addressPO);

    void update(AddressPO addressPO);

    void delete(@Param("userId") String userId, @Param("addressId") String addressId);

    void clearDefault(@Param("userId") String userId);

    AddressPO queryAddressByAddressId(@Param("userId") String userId, @Param("addressId") String addressId);

    List<AddressPO> queryAddressListByUserId(@Param("userId") String userId);

}
