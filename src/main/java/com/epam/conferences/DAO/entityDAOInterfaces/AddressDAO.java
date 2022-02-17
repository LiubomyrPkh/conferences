package com.epam.conferences.DAO.entityDAOInterfaces;

import com.epam.conferences.DAO.entityData.Address;

import java.util.List;
import java.util.Set;

public interface AddressDAO {
    int insertAddress(Address address);
    boolean deleteAddress(int addressId);
    Address findAddress(int addressId);
    boolean updateAddress(Address address);
    Set<Address> selectAddressRowSet(Address criteria);
    List<Address> selectAddressList(Address criteria);
}
