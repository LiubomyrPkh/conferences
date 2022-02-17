package com.epam.conferences.DAO;

import com.epam.conferences.DAO.entityDAOInterfaces.*;

public abstract class DAOFactory {

    public enum DBTypes {
        MySQL
    }

    public abstract EventDAO getEventDAO();
    public abstract AddressDAO getAddressDAO();
    public abstract PersonDAO getPersonDAO();
    public abstract UserEventDAO getUserEventDAO();

    public static DAOFactory getDAOFactory(DBTypes whichFactory) {

        switch (whichFactory) {
            case MySQL: return MySQLDAOFactory.getInstance();
            default: return MySQLDAOFactory.getInstance();
        }

    }
}
