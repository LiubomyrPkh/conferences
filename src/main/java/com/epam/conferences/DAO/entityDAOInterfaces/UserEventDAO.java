package com.epam.conferences.DAO.entityDAOInterfaces;

import com.epam.conferences.DAO.entityData.Event;
import com.epam.conferences.DAO.entityData.Person;

import java.util.Map;

public interface UserEventDAO {
    Map<Event, Integer> getEventsByUserId(int userId);
    Map<Person, Integer> getUsersByEventId(int eventDateId);
    int insertUserEvent(int userId, int eventDateId);
    //Map<Event, Person> getAll();
    // updateUserEvent(int userId, int eventId, int UserEventId);
    boolean deleteUserEvent(int userId, int eventDateId);
    Map<String, Integer> getUsersCountInEvent();
}
