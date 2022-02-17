package com.epam.conferences.DAO.MySQLEntityDAO;

import com.epam.conferences.DAO.MySQLDAOFactory;
import com.epam.conferences.DAO.entityDAOInterfaces.AddressDAO;
import com.epam.conferences.DAO.entityData.Address;

import java.sql.*;
import java.util.*;

public class MySQLAddressDAO implements AddressDAO {

    enum Queries {
        INSERT_ADDRESS("INSERT INTO address (country, city, street, house_num, flat_num) VALUES (?, ?, ?, ?, ?)"),
        DELETE_ADDRESS("DELETE FROM address WHERE address_id = ?"),
        FIND_ADDRESS("SELECT * FROM address WHERE address_id = ?"),
        UPDATE_ADDRESS("UPDATE address SET country = ?, city = ?, street = ?, house_num = ?, flat_num = ? WHERE address_id = ?"),
        SELECT_ADDRESS("SELECT * FROM address"),
        SELECT_ADDRESS_BY_PARAMS("SELECT country, city, street, house_num, flat_num, address_id FROM address WHERE");

        private final String query;

        Queries(String query) {
            this.query = query;
        }
    }

    @Override
    public int insertAddress(Address address) {
        int insertedId = 0;
        try (Connection connection = MySQLDAOFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(Queries.INSERT_ADDRESS.query,
                Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1,address.getCountry());
            statement.setString(2,address.getCity());
            statement.setString(3,address.getStreet());
            statement.setInt(4,address.getHouseNum());
            statement.setInt(5,address.getFlatNum());

            int rowsAffected = statement.executeUpdate();

            insertedId = MySQLCRUDUtils.idAfterInsert(statement, rowsAffected);

            address.setAddressId(insertedId);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return insertedId;
    }

    @Override
    public boolean deleteAddress(int addressId) {
        boolean res = false;
        try(Connection connection = MySQLDAOFactory.createConnection();
                PreparedStatement statement = connection.prepareStatement(Queries.DELETE_ADDRESS.query)) {
            statement.setInt(1, addressId);
            res = statement.executeUpdate() > 0;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return res;
    }

    @Override
    public Address findAddress(int addressId) {
        Address address = null;

        try(Connection connection = MySQLDAOFactory.createConnection();
                PreparedStatement statement = connection.prepareStatement(Queries.FIND_ADDRESS.query)) {

            statement.setInt(1, addressId);
            ResultSet res = statement.executeQuery();

            if(res.next()) {
                address = MySQLCRUDUtils.fillAddress(res);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return address;
    }

    @Override
    public boolean updateAddress(Address address) {
        boolean res = false;

        try(Connection connection = MySQLDAOFactory.createConnection();
                PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_ADDRESS.query)){

            statement.setString(1, address.getCountry());
            statement.setString(2, address.getCity());
            statement.setString(3, address.getStreet());
            statement.setInt(4, address.getHouseNum());
            statement.setInt(5, address.getFlatNum());
            statement.setInt(6, address.getAddressId());

            res = statement.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return res;
    }

    @Override
    public Set<Address> selectAddressRowSet(Address criteria) {
        Set<Address> set = new TreeSet<>(Comparator.comparingInt(Address::getAddressId));
        set.addAll(this.selectAddressList(criteria));
        return set;
    }

    @Override
    public List<Address> selectAddressList(Address criteria) {
        ArrayList<Address> list = new ArrayList<>();

        try(Connection connection = MySQLDAOFactory.createConnection();
                PreparedStatement statement = connection.prepareStatement(queryCriteriaFill(criteria))) {

            int paramIndex = 1;

            if (criteria != null) {
                if(criteria.getAddressId() != 0) {
                    statement.setInt(paramIndex++, criteria.getAddressId());
                }

                if(criteria.getCountry() != null) {
                    statement.setString(paramIndex++, criteria.getCountry());
                }

                if(criteria.getCity() != null) {
                    statement.setString(paramIndex++, criteria.getCity());
                }

                if(criteria.getStreet() != null) {
                    statement.setString(paramIndex++, criteria.getStreet());
                }

                if(criteria.getHouseNum() != 0) {
                    statement.setInt(paramIndex++, criteria.getHouseNum());
                }

                if(criteria.getFlatNum() != 0) {
                    statement.setInt(paramIndex, criteria.getFlatNum());
                }
            }

            ResultSet res = statement.executeQuery();

            while(res.next()) {
                list.add(MySQLCRUDUtils.fillAddress(res));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }

    private String queryCriteriaFill(Address criteria){
        StringBuilder query = new StringBuilder(Queries.SELECT_ADDRESS_BY_PARAMS.query);

        if(criteria == null) {
            return Queries.SELECT_ADDRESS.query;
        }

        if(criteria.getAddressId() != 0) {
            query.append(" address_id = ?").append(" AND");
        }

        if(criteria.getCountry() != null) {
            query.append(" country = ?").append(" AND");
        }

        if(criteria.getCity() != null) {
            query.append(" city = ?").append(" AND");
        }

        if(criteria.getStreet() != null) {
            query.append(" street = ?").append(" AND");
        }

        if(criteria.getHouseNum() != 0) {
            query.append(" house_num = ?").append(" AND");
        }

        if(criteria.getFlatNum() != 0) {
            query.append(" flat_num = ?").append(" AND");
        }

        return MySQLCRUDUtils.removeLastAndInQuery(query.toString());
    }
}
