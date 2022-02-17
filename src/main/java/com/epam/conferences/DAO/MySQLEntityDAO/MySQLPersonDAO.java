package com.epam.conferences.DAO.MySQLEntityDAO;

import com.epam.conferences.DAO.MySQLDAOFactory;
import com.epam.conferences.DAO.entityDAOInterfaces.PersonDAO;
import com.epam.conferences.DAO.entityData.Person;

import java.sql.*;
import java.util.*;

public class MySQLPersonDAO implements PersonDAO {

    enum Queries {
        INSERT_PERSON("INSERT INTO person (name, surname, email, password, role) VALUES (?, ?, ?, ?, ?)"),
        DELETE_PERSON("DELETE FROM person WHERE person_id = ?"),
        FIND_PERSON("SELECT * FROM person WHERE person_id = ?"),
        UPDATE_PERSON("UPDATE person SET name = ?, surname = ?, email = ?, password = ?, role = ? WHERE person_id = ?"),
        SELECT_PERSON("SELECT * FROM person"),
        SELECT_PERSON_BY_PARAMS("SELECT person_id, name, surname, email, password, role FROM person WHERE");

        private final String query;

        Queries(String query) {
            this.query = query;
        }
    }

    @Override
    public int insertPerson(Person person){
        int insertedId = 0;
        try (Connection connection = MySQLDAOFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(Queries.INSERT_PERSON.query,
                Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, person.getName());
            statement.setString(2, person.getSurname());
            statement.setString(3, person.getEmail());
            statement.setString(4, person.getPassword());
            statement.setString(5, person.getRole());

            int rowsAffected = statement.executeUpdate();

            insertedId = MySQLCRUDUtils.idAfterInsert(statement, rowsAffected);

            person.setPersonId(insertedId);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return insertedId;
    }

    @Override
    public boolean deletePerson(int personId){
        boolean res = false;
        try(Connection connection = MySQLDAOFactory.createConnection();
            PreparedStatement statement = connection.prepareStatement(Queries.DELETE_PERSON.query)) {
            statement.setInt(1, personId);
            res = statement.executeUpdate() > 0;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return res;
    }

    @Override
    public Person findPerson(int personId){
        Person person = null;

        try(Connection connection = MySQLDAOFactory.createConnection();
            PreparedStatement statement = connection.prepareStatement(Queries.FIND_PERSON.query)) {

            statement.setInt(1, personId);
            ResultSet res = statement.executeQuery();

            if(res.next()) {
                person = MySQLCRUDUtils.fillPerson(res);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return person;
    }

    @Override
    public boolean updatePerson(Person person){
        boolean res = false;

        try(Connection connection = MySQLDAOFactory.createConnection();
            PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_PERSON.query)){

            statement.setString(1, person.getName());
            statement.setString(2, person.getSurname());
            statement.setString(3, person.getEmail());
            statement.setString(4, person.getPassword());
            statement.setString(5, person.getRole());

            res = statement.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return res;
    }

    @Override
    public Set<Person> selectPersonRowSet(Person criteria){
        Set<Person> set = new TreeSet<>(Comparator.comparingInt(Person::getPersonId));
        set.addAll(this.selectPersonList(criteria));
        return set;
    }

    @Override
    public List<Person> selectPersonList(Person criteria){
        ArrayList<Person> list = new ArrayList<>();

        try(Connection connection = MySQLDAOFactory.createConnection();
            PreparedStatement statement = connection.prepareStatement(queryCriteriaFill(criteria))) {

            int paramIndex = 1;

            if (criteria != null) {
                if (criteria.getPersonId() != 0) {
                    statement.setInt(paramIndex++, criteria.getPersonId());
                }

                if (criteria.getName() != null) {
                    statement.setString(paramIndex++, criteria.getName());
                }

                if (criteria.getSurname() != null) {
                    statement.setString(paramIndex++, criteria.getSurname());
                }

                if (criteria.getEmail() != null) {
                    statement.setString(paramIndex++, criteria.getEmail());
                }

                if (criteria.getPassword() != null) {
                    statement.setString(paramIndex++, criteria.getPassword());
                }

                if (criteria.getRole() != null) {
                    statement.setString(paramIndex, criteria.getRole());
                }
            }

            ResultSet res = statement.executeQuery();

            while(res.next()) {
                list.add(MySQLCRUDUtils.fillPerson(res));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }

    private String queryCriteriaFill(Person criteria) {

        StringBuilder selectors = new StringBuilder(Queries.SELECT_PERSON_BY_PARAMS.query);

        if(criteria == null) {
            return Queries.SELECT_PERSON.query;
        }

        if(criteria.getPersonId() != 0) {
            selectors.append(" person_id = ?").append(" AND");
        }

        if(criteria.getName() != null) {
            selectors.append(" name = ?").append(" AND");
        }

        if(criteria.getSurname() != null) {
            selectors.append(" surname = ?").append(" AND");
        }

        if(criteria.getEmail() != null) {
            selectors.append(" email = ?").append(" AND");
        }

        if(criteria.getPassword() != null) {
            selectors.append(" password = ?").append(" AND");
        }

        if(criteria.getRole() != null) {
            selectors.append(" role = ?").append(" AND");
        }

        return MySQLCRUDUtils.removeLastAndInQuery(selectors.toString());
    }
}
