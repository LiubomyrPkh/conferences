package com.epam.conferences.DAO.MySQLEntityDAO;

import com.epam.conferences.DAO.entityData.Address;
import com.epam.conferences.DAO.entityData.Event;
import com.epam.conferences.DAO.entityData.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MySQLCRUDUtils {
    public static String sqlDateToString(java.sql.Timestamp timestamp) {
        return new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(timestamp.getTime()));
    }

    public static java.sql.Timestamp stringToSQLDate(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");
        Date date = sdf.parse(str);
        return  new java.sql.Timestamp(date.getTime());
    }

    public static int idAfterInsert(Statement state, int rowsAffected) throws SQLException {
        int insertedId = 0;
        if(rowsAffected == 0) {
            throw new SQLException("Creating failed, no rows affected.");
        }

        try (ResultSet generatedKeys = state.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                insertedId = generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating failed, no ID obtained.");
            }
        }

    return insertedId;
    }

    public static String removeLastAndInQuery(String query) {
        String[] stringArr = query.split(" ");

        if(stringArr[stringArr.length - 1].equals("AND")) {
            stringArr[stringArr.length - 1] = "";
        }

        return String.join(" ", stringArr);
    }

    public static Address fillAddress(ResultSet res) throws SQLException {
        return new Address(res.getInt("address_id"),
                res.getString("country"),
                res.getString("city"),
                res.getString("street"),
                res.getInt("house_num"),
                res.getInt("flat_num"));
    }

    public static Person fillPerson(ResultSet res) throws SQLException {
        return new Person(res.getInt("person_id"),
                res.getString("name"),
                res.getString("surname"),
                res.getString("email"),
                res.getString("password"),
                res.getString("role"));
    }
}
