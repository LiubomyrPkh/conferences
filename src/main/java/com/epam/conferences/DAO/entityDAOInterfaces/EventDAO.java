package com.epam.conferences.DAO.entityDAOInterfaces;

import com.epam.conferences.DAO.entityData.Event;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface EventDAO {
    int insertEvent(Event event);
    boolean deleteEvent(int eventId);
    Event findEvent(int eventId);
    boolean updateEvent(Event event);
    Set<Event> selectEventRowSet(Event criteria);
    List<Event> selectEventList(Event criteria);
    String getEventDescription(int eventId);
    boolean insertUpdateDescription(String description, int eventId) throws SQLException;
    int getEventDateId(String date);
    int insertProposedReport(Event event) throws SQLException;
    List<Event> selectFreeReportList() throws SQLException;
    int insertModeratorEvent(Event event) throws SQLException;
    List<Event> selectAllEventsWithNullModerator() throws SQLException;
    List<Event> selectAllEventsByDate(String date) throws SQLException;
}
