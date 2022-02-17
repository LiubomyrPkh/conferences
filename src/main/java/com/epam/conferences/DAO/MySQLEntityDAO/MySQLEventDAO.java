package com.epam.conferences.DAO.MySQLEntityDAO;

import com.epam.conferences.DAO.DAOFactory;
import com.epam.conferences.DAO.MySQLDAOFactory;
import com.epam.conferences.DAO.entityDAOInterfaces.EventDAO;
import com.epam.conferences.DAO.entityData.Address;
import com.epam.conferences.DAO.entityData.Event;
import com.epam.conferences.DAO.entityData.Person;

import java.sql.*;
import java.text.ParseException;
import java.util.*;

public class MySQLEventDAO implements EventDAO {

    enum Queries {
        INSERT_EVENT("INSERT INTO event (createdByModeratorId, event_date_id, event_address_id, speaker_id, title) VALUES (?, ?, ?, ?, ?)"),
        DELETE_EVENT("DELETE FROM event WHERE event_id = ?"),
        FIND_EVENT("SELECT ev.createdByModeratorId, evd.event_date, ev.event_address_id, ev.speaker_id, ev.title" +
                " FROM event as ev JOIN event_date as evd ON evd.event_date_id = ev.event_date_id WHERE ev.event_id = ?"),
        UPDATE_EVENT("UPDATE event SET createdByModeratorId = ?, event_date_id = ?, event_address_id = ?, speaker_id = ?, title = ? WHERE event_id = ?"),
        SELECT_EVENT("SELECT * FROM event as ev JOIN event_date as evd On evd.event_date_id = ev.event_date_id" +
                " WHERE ev.createdByModeratorId is not null AND ev.speaker_id is not null"),
        SELECT_EVENT_BY_PARAMS("SELECT ev.createdByModeratorId, evd.event_date, ev.event_address_id, ev.event_id, ev.speaker_id, ev.title" +
                " FROM event as ev JOIN event_date as evd ON evd.event_date_id = ev.event_date_id WHERE"),
        SELECT_EVENT_DATE_ID_BY_DATE("SELECT * FROM Event_Date WHERE event_date = ?"),
        SELECT_EVENT_DATE("SELECT * FROM Event_Date"),
        INSERT_EVENT_DATE("INSERT INTO Event_Date (event_date) VALUES (?)"),
        UPDATE_EVENT_DATE("UPDATE event_date SET event_date = ? WHERE event_date_id = ?"),
        DELETE_EVENT_DATE("DELETE FROM event_date WHERE event_date_id = ?"),
        SELECT_EVENT_DESCRIPTION("SELECT evdes.event_description FROM Event_Description as evdes" +
                " JOIN Event_Date as evd ON evd.event_date_id = evdes.event_date_id" +
                " JOIN Event as ev ON ev.event_date_id = evd.event_date_id WHERE ev.event_id = ?"),
        INSERT_EVENT_DESCRIPTION("INSERT INTO Event_Description (event_date_id, event_description) VALUES (?, ?)"),
        UPDATE_EVENT_DESCRIPTION("UPDATE Event_Description SET event_date_id = ?, event_description = ? WHERE event_description_id = ?"),
        INSERT_PROPOSED_REPORT("INSERT INTO event (createdByModeratorId, event_date_id, event_address_id, speaker_id, title) VALUES (null,?, ?, ?, ?)"),
        SELECT_FREE_REPORT("SELECT * FROM event as ev JOIN event_date as evd ON ev.event_date_id = evd.event_date_id" +
                " WHERE createdByModeratorId is not null AND speaker_id is null"),
        INSERT_MODERATOR_EVENT("INSERT INTO event (speaker_id, event_date_id, event_address_id, createdByModeratorId, title) VALUES (null,?, ?, ?, ?)"),
        SELECT_ALL_EVENTS_WITH_NULL_MODERATOR("SELECT * FROM event as ev JOIN event_date as evd ON ev.event_date_id = evd.event_date_id" +
                " JOIN person as per ON per.person_id = ev.speaker_id" +
                " JOIN address as addr ON addr.address_id = ev.event_address_id" +
                " WHERE createdByModeratorId is null"),
        SELECT_ALL_EVENTS_BY_DATE("SELECT * FROM event as ev" +
                " JOIN event_date as evd ON evd.event_date_id = ev.event_date_id" +
                " JOIN address as addr ON addr.address_id = ev.event_address_id WHERE event_date = ?");

        private final String query;

        Queries(String query) {
            this.query = query;
        }
    }

    @Override
    public int insertEvent(Event event){
        int insertedId = 0;
        try (Connection connection = MySQLDAOFactory.createConnection();
                PreparedStatement statement = connection.prepareStatement(Queries.INSERT_EVENT.query,
                Statement.RETURN_GENERATED_KEYS)) {

            int eventDateId = getEventDateId(event.getEventDate());

            List<Address> addresses = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getAddressDAO().selectAddressList(event.getAddress());
            int addressId = addresses.isEmpty() ? DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL)
                    .getAddressDAO().insertAddress(event.getAddress()) : addresses.get(0).getAddressId();


            statement.setInt(1, event.getModerator().getPersonId());
            statement.setInt(2, eventDateId);
            statement.setInt(3, addressId);
            statement.setInt(4, event.getSpeaker().getPersonId());
            statement.setString(5, event.getTitle());

            int rowsAffected = statement.executeUpdate();

            insertedId = MySQLCRUDUtils.idAfterInsert(statement, rowsAffected);

            event.setEventId(insertedId);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return insertedId;
    }

    @Override
    public boolean deleteEvent(int eventId){
        boolean res = false;
        try(Connection connection = MySQLDAOFactory.createConnection();
                PreparedStatement statement = connection.prepareStatement(Queries.DELETE_EVENT.query)) {
            statement.setInt(1, eventId);
            res = statement.executeUpdate() > 0;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return res;
    }

    @Override
    public Event findEvent(int eventId){
        Event event = null;

        try(Connection connection = MySQLDAOFactory.createConnection();
                PreparedStatement statement = connection.prepareStatement(Queries.FIND_EVENT.query)) {

            statement.setInt(1, eventId);
            ResultSet res = statement.executeQuery();

            if(res.next()) {
                Address address = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getAddressDAO().findAddress(res.getInt("event_address_id"));
                Person moderator = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getPersonDAO().findPerson(res.getInt("createdByModeratorId"));
                Person speaker = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getPersonDAO().findPerson(res.getInt("speaker_id"));
                event = new Event(eventId, moderator,
                        MySQLCRUDUtils.sqlDateToString(res.getTimestamp("event_date")),
                        address, speaker, res.getString("title"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return event;
    }

    @Override
    public boolean updateEvent(Event event){
        boolean res = false;

        try(Connection connection = MySQLDAOFactory.createConnection();
                PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_EVENT.query)){

            int dateId = this.getEventDateId(event.getEventDate());

            statement.setInt(1, event.getModerator().getPersonId());
            statement.setInt(2, dateId);
            statement.setInt(3, event.getAddress().getAddressId());
            statement.setInt(4, event.getSpeaker().getPersonId());
            statement.setString(5, event.getTitle());
            statement.setInt(6, event.getEventId());

            res = statement.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return res;
    }

    @Override
    public Set<Event> selectEventRowSet(Event criteria){
        Set<Event> set = new TreeSet<>(Comparator.comparingInt(Event::getEventId));
        set.addAll(this.selectEventList(criteria));
        return set;
    }

    @Override
    public List<Event> selectEventList(Event criteria){
        ArrayList<Event> list = new ArrayList<>();

        try(Connection connection = MySQLDAOFactory.createConnection();
                PreparedStatement statement = connection.prepareStatement(queryCriteriaFill(criteria))) {

            int paramIndex = 1;

            if (criteria != null) {
                if(criteria.getModerator() != null) {
                    statement.setInt(paramIndex++, criteria.getModerator().getPersonId());
                }

                if(criteria.getEventDate() != null) {
                    statement.setTimestamp(paramIndex++, MySQLCRUDUtils.stringToSQLDate(criteria.getEventDate()));
                }

                if(criteria.getAddress() != null) {
                    statement.setInt(paramIndex++, criteria.getAddress().getAddressId());
                }

                if(criteria.getEventId() != 0) {
                    statement.setInt(paramIndex++, criteria.getEventId());
                }

                if(criteria.getSpeaker() != null) {
                    statement.setInt(paramIndex++, criteria.getSpeaker().getPersonId());
                }

                if(criteria.getTitle() != null) {
                    statement.setString(paramIndex, criteria.getTitle());
                }
            }

            ResultSet res = statement.executeQuery();

            while(res.next()) {
                Address address = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getAddressDAO().findAddress(res.getInt("event_address_id"));
                Person moderator = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getPersonDAO().findPerson(res.getInt("createdByModeratorId"));
                Person speaker = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getPersonDAO().findPerson(res.getInt("speaker_id"));

                java.sql.Timestamp date = res.getTimestamp("event_date");
                String dateStr = MySQLCRUDUtils.sqlDateToString(date);

                list.add(new Event(res.getInt("event_id"),
                        moderator,
                        dateStr,
                        address, speaker, res.getString("title")));
            }

        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }

        return list;
    }

    private String queryCriteriaFill(Event criteria) {
        StringBuilder query = new StringBuilder(Queries.SELECT_EVENT_BY_PARAMS.query);

        if(criteria == null) {
            return Queries.SELECT_EVENT.query;
        }

        if(criteria.getModerator() != null) {
            query.append(" createdByModeratorId = ?").append(" AND");
        }

        if(criteria.getEventDate() != null) {
            query.append(" event_date = ?").append(" AND");
        }

        if(criteria.getAddress() != null) {
            query.append(" event_address_id = ?").append(" AND");
        }

        if(criteria.getEventId() != 0) {
            query.append(" event_id = ?").append(" AND");
        }

        if(criteria.getSpeaker() != null) {
            query.append(" speaker_id = ?").append(" AND");
        }

        if(criteria.getTitle() != null) {
            query.append(" title = ?").append(" AND");
        }

        return MySQLCRUDUtils.removeLastAndInQuery(query.toString());
    }

    public int getEventDateId(String date) {
        int insertedId = 0;

        try(Connection connection = MySQLDAOFactory.createConnection();
            PreparedStatement statementInsert = connection.prepareStatement(Queries.INSERT_EVENT_DATE.query, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement statementSelect = connection.prepareStatement(Queries.SELECT_EVENT_DATE_ID_BY_DATE.query)){

            statementSelect.setTimestamp(1,MySQLCRUDUtils.stringToSQLDate(date));

            ResultSet res = statementSelect.executeQuery();

            if(res.next()) {
                return res.getInt("event_date_id");
            }

            statementInsert.setTimestamp(1, MySQLCRUDUtils.stringToSQLDate(date));
            int rowsAffected = statementInsert.executeUpdate();

            insertedId = MySQLCRUDUtils.idAfterInsert(statementInsert, rowsAffected);

        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }

        return insertedId;
    }

    @Override
    public String getEventDescription(int eventId) {
        String description = null;

        try(Connection connection = MySQLDAOFactory.createConnection();
            PreparedStatement statement = connection.prepareStatement(Queries.SELECT_EVENT_DESCRIPTION.query)){

            statement.setInt(1, eventId);
            ResultSet res = statement.executeQuery();

            if(res.next()) {
                description = res.getString("event_description");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return description;
    }

    @Override
    public boolean insertUpdateDescription(String description, int eventId) throws SQLException {
        boolean success;

        try(Connection connection = MySQLDAOFactory.createConnection();
            PreparedStatement statementUpdate = connection.prepareStatement(Queries.UPDATE_EVENT_DESCRIPTION.query);
            PreparedStatement statementInsert = connection.prepareStatement(Queries.INSERT_EVENT_DESCRIPTION.query);
            Statement statementSelect = connection.createStatement()) {

            Event event = this.findEvent(eventId);

            if(event == null) {
                throw new SQLException("Event does not exist");
            }

            int eventDateId = this.getEventDateId(event.getEventDate());
            ResultSet res = statementSelect.executeQuery(Queries.SELECT_EVENT_DESCRIPTION.query);

            if(res.next()) {
                statementUpdate.setInt(1, eventDateId);
                statementUpdate.setString(2, description);
            } else {
                statementInsert.setInt(1, eventDateId);
                statementInsert.setString(2, description);
                statementInsert.setInt(3, eventId);
            }
            success = statementUpdate.executeUpdate() > 0;
        }
        return success;
    }

    @Override
    public int insertProposedReport(Event event) throws SQLException {
        int insertedId;
        try (Connection connection = MySQLDAOFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(Queries.INSERT_PROPOSED_REPORT.query,
                     Statement.RETURN_GENERATED_KEYS)) {

            int eventDateId = getEventDateId(event.getEventDate());

            List<Address> addresses = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getAddressDAO().selectAddressList(event.getAddress());
            int addressId = addresses.isEmpty() ? DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL)
                    .getAddressDAO().insertAddress(event.getAddress()) : addresses.get(0).getAddressId();

            statement.setInt(1, eventDateId);
            statement.setInt(2, addressId);
            statement.setInt(3, event.getSpeaker().getPersonId());
            statement.setString(4, event.getTitle());

            int rowsAffected = statement.executeUpdate();

            insertedId = MySQLCRUDUtils.idAfterInsert(statement, rowsAffected);

            event.setEventId(insertedId);

        }
        return insertedId;
    }

    @Override
    public List<Event> selectFreeReportList() throws SQLException {
       List<Event> eventList = new ArrayList<>();

       try(Connection connection = MySQLDAOFactory.createConnection();
        Statement statement = connection.createStatement()) {
           ResultSet res = statement.executeQuery(Queries.SELECT_FREE_REPORT.query);
           while (res.next()) {
               Event event = new Event();
               Person moderator = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getPersonDAO().findPerson(res.getInt("createdByModeratorId"));
               Address address = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getAddressDAO().findAddress(res.getInt("event_address_id"));
               event.setEventId(res.getInt("event_id"));
               event.setEventDate(MySQLCRUDUtils.sqlDateToString(res.getTimestamp("event_date")));
               event.setTitle(res.getString("title"));
               event.setModerator(moderator);
               event.setAddress(address);
               eventList.add(event);
           }
       }
       return eventList;
    }

    @Override
    public int insertModeratorEvent(Event event) throws SQLException {
        int insertedId;
        try (Connection connection = MySQLDAOFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(Queries.INSERT_MODERATOR_EVENT.query,
                     Statement.RETURN_GENERATED_KEYS)) {

            int eventDateId = getEventDateId(event.getEventDate());

            List<Address> addresses = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getAddressDAO().selectAddressList(event.getAddress());
            int addressId = addresses.isEmpty() ? DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL)
                    .getAddressDAO().insertAddress(event.getAddress()) : addresses.get(0).getAddressId();

            statement.setInt(1, eventDateId);
            statement.setInt(2, addressId);
            statement.setInt(3, event.getModerator().getPersonId());
            statement.setString(4, event.getTitle());

            int rowsAffected = statement.executeUpdate();

            insertedId = MySQLCRUDUtils.idAfterInsert(statement, rowsAffected);

            event.setEventId(insertedId);

        }
        return insertedId;
    }

    @Override
    public List<Event> selectAllEventsWithNullModerator() throws SQLException {
        List<Event> eventList = new ArrayList<>();
        try(Connection connection = MySQLDAOFactory.createConnection();
            Statement statement = connection.createStatement()) {
            ResultSet res = statement.executeQuery(Queries.SELECT_ALL_EVENTS_WITH_NULL_MODERATOR.query);
            while (res.next()) {
                Event event = new Event();
                Person speaker = MySQLCRUDUtils.fillPerson(res);
                Address address = MySQLCRUDUtils.fillAddress(res);
                event.setEventId(res.getInt("event_id"));
                event.setEventDate(MySQLCRUDUtils.sqlDateToString(res.getTimestamp("event_date")));
                event.setTitle(res.getString("title"));
                event.setSpeaker(speaker);
                event.setAddress(address);
                eventList.add(event);
            }
        }
        return eventList;
    }

    @Override
    public List<Event> selectAllEventsByDate(String date) throws SQLException {
        List<Event> eventList = new ArrayList<>();
        try(Connection connection = MySQLDAOFactory.createConnection();
            PreparedStatement statement = connection.prepareStatement(Queries.SELECT_ALL_EVENTS_BY_DATE.query)) {
            statement.setTimestamp(1, MySQLCRUDUtils.stringToSQLDate(date));
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                Event event = new Event();
                Person speaker = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getPersonDAO().findPerson(res.getInt("speaker_id"));
                Person moderator = DAOFactory.getDAOFactory(DAOFactory.DBTypes.MySQL).getPersonDAO().findPerson(res.getInt("createdByModeratorId"));
                Address address = MySQLCRUDUtils.fillAddress(res);
                event.setEventId(res.getInt("event_id"));
                event.setEventDate(MySQLCRUDUtils.sqlDateToString(res.getTimestamp("event_date")));
                event.setTitle(res.getString("title"));
                event.setSpeaker(speaker);
                event.setAddress(address);
                event.setModerator(moderator);
                eventList.add(event);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return eventList;
    }
}
