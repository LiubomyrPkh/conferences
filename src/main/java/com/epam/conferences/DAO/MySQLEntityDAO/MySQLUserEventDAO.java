package com.epam.conferences.DAO.MySQLEntityDAO;

import com.epam.conferences.DAO.DAOFactory;
import com.epam.conferences.DAO.MySQLDAOFactory;
import com.epam.conferences.DAO.entityDAOInterfaces.UserEventDAO;
import com.epam.conferences.DAO.entityData.Event;
import com.epam.conferences.DAO.entityData.Person;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MySQLUserEventDAO implements UserEventDAO {

    enum Queries {
        USERS_BY_EVENT("SELECT ue.user_id, ue.user_event_id, per.person_id FROM User_Event as ue" +
                " JOIN Person as per ON per.person_id = ue.user_id WHERE event_date_id = ?"),
        EVENTS_BY_USER("SELECT ue.user_event_id, ue.event_date_id, evn.event_id FROM User_Event as ue" +
                " JOIN Event as evn ON evn.event_date_id = ue.event_date_id WHERE user_id = ?"),
        SELECT_ALL("SELECT * FROM User_Event"),
        SELECT_BY_PARAMS("SELECT user_event_id, user_id, event_id FROM User_Event WHERE"),
        INSERT_USER_EVENT("INSERT INTO User_Event (user_id, event_date_id) VALUES (?, ?)"),
        UPDATE_USER_EVENT("UPDATE User_Event SET user_id = ?, event_id = ? WHERE user_event_id = ?"),
        DELETE_USER_EVENT("DELETE FROM User_Event WHERE user_id = ? AND event_date_id = ?"),
        SELECT_ALL_USER_EVENT("SELECT * FROM User_Event as ue" +
                " JOIN event_date as evd ON evd.event_date_id = ue.event_date_id" +
                " JOIN Event as ev ON ev.event_date_id = evd.event_date_id" +
                " JOIN person as per ON per.person_id = ue.user_id"),
        GET_USERS_COUNT_IN_EVENT("SELECT event_date, COUNT(*) as count FROM User_Event as ue" +
                " JOIN event_date as evd ON ue.event_date_id = evd.event_date_id" +
                " GROUP BY event_date");

        private final String query;

        Queries(String query) {
            this.query = query;
        }
    }


    @Override
    public Map<Event, Integer> getEventsByUserId(int userId) {
        HashMap<Event, Integer> map = new HashMap<>();

        try(Connection connection = MySQLDAOFactory.createConnection();
            PreparedStatement statement = connection.prepareStatement(Queries.EVENTS_BY_USER.query)) {

            statement.setInt(1, userId);

            ResultSet res = statement.executeQuery();

            while(res.next()) {
                Event event = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getEventDAO().findEvent(res.getInt("event_id"));
                map.put(event, res.getInt("user_event_id"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return map;
    }

    @Override
    public Map<Person, Integer> getUsersByEventId(int eventId) {
        HashMap<Person, Integer> map = new HashMap<>();

        try(Connection connection = MySQLDAOFactory.createConnection();
            PreparedStatement statement = connection.prepareStatement(Queries.USERS_BY_EVENT.query)) {

            statement.setInt(1, eventId);

            ResultSet res = statement.executeQuery();

            while(res.next()) {
                Person user = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getPersonDAO().findPerson(res.getInt("user_id"));
                map.put(user, res.getInt("user_event_id"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public int insertUserEvent(int userId, int eventDateId) {
        int insertedId = 0;

        try(Connection connection = MySQLDAOFactory.createConnection();
            PreparedStatement statement = connection.prepareStatement(Queries.INSERT_USER_EVENT.query,
                    Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, userId);
            statement.setInt(2, eventDateId);

            int rowsAffected = statement.executeUpdate();

            insertedId = MySQLCRUDUtils.idAfterInsert(statement, rowsAffected);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return insertedId;
    }


    @Override
    public boolean deleteUserEvent(int userId, int eventDateId) {
        boolean res = false;
        try(Connection connection = MySQLDAOFactory.createConnection();
            PreparedStatement statement = connection.prepareStatement(Queries.DELETE_USER_EVENT.query)) {
            statement.setInt(1, userId);
            statement.setInt(2, eventDateId);
            res = statement.executeUpdate() > 0;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return res;
    }

    @Override
    public Map<String, Integer> getUsersCountInEvent() {
       Map<String, Integer> map = new HashMap<>();

        try(Connection connection = MySQLDAOFactory.createConnection();
            Statement statement = connection.createStatement()) {

            ResultSet res = statement.executeQuery(Queries.GET_USERS_COUNT_IN_EVENT.query);

            while(res.next()) {
                map.put(MySQLCRUDUtils.sqlDateToString(res.getTimestamp("event_date")),
                        res.getInt("count"));
            }

        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return map;
    }
/*
    @Override
    public Map<Event, Person> getAll(Event criteria) {
        return null;
    }

    @Override
    public boolean insertUserEvent(Map<Event, Person> map) {
        return false;
    }

    @Override
    public boolean updateUserEvent(Person user, Event event, int PersonEventId) {
        return false;
    }*/
}
