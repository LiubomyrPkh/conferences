package com.epam.conferences.DAO.entityData;

import java.io.Serializable;
import java.util.Objects;

public class Person implements Serializable {
    private int personId;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String role;

    public enum Role {
        USER("user"),
        SPEAKER("speaker"),
        MODERATOR("moderator");

        private final String role;

        private Role(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }
    }

    public Person(int personId, String name, String surname, String email, String password, String role) {
        this.personId = personId;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Person(String name, String surname, String email, String password, String role) {
        this.personId = 0;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Person() {
        this.personId = 0;
        this.name = null;
        this.surname = null;
        this.email = null;
        this.password = null;
        this.role = null;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) && Objects.equals(surname, person.surname)
                && Objects.equals(email, person.email) && Objects.equals(password, person.password)
                && Objects.equals(role, person.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, email, password, role);
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }
}
