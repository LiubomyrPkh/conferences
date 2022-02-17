package com.epam.conferences.DAO.entityDAOInterfaces;

import com.epam.conferences.DAO.entityData.Person;

import java.util.List;
import java.util.Set;

public interface PersonDAO {
    int insertPerson(Person person);
    boolean deletePerson(int personId);
    Person findPerson(int personId);
    boolean updatePerson(Person person);
    Set<Person> selectPersonRowSet(Person criteria);
    List<Person> selectPersonList(Person criteria);
}
