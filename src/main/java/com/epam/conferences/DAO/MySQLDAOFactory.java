package com.epam.conferences.DAO;

import com.epam.conferences.DAO.MySQLEntityDAO.*;
import com.epam.conferences.DAO.entityDAOInterfaces.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

public class MySQLDAOFactory extends DAOFactory {

    private static DataSource dataSource;
    private static MySQLDAOFactory instance;

    public static Connection createConnection() {
            try {
                if(dataSource == null) {
                    InitialContext context = new InitialContext();
                    dataSource = (DataSource) context.lookup("java:/comp/env/jdbc/mysql");
                }
                return dataSource.getConnection();
            } catch (SQLException | NamingException ex) {
                ex.printStackTrace();
            }
        return null;
    }

    public static MySQLDAOFactory getInstance() {
        if(instance == null) {
            instance = new MySQLDAOFactory();
        }
        return instance;
    }

    private MySQLDAOFactory() {}


    public EventDAO getEventDAO() {
        return new MySQLEventDAO();
    }

    public AddressDAO getAddressDAO() {
        return new MySQLAddressDAO();
    }

    public PersonDAO getPersonDAO() {
        return new MySQLPersonDAO();
    }

    public UserEventDAO getUserEventDAO() {
        return new MySQLUserEventDAO();
    }
}