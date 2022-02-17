package com.epam.conferences.DAO.entityData;

import java.io.Serializable;
import java.util.Objects;

public class Address implements Serializable {
    int addressId;
    String country;
    String city;
    String street;
    int houseNum;
    int flatNum;

    public Address(String country, String city,
                   String street, int houseNum, int flatNum) {
        this.addressId = 0;
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseNum = houseNum;
        this.flatNum = flatNum;
    }

    public Address(int addressId, String country, String city,
                   String street, int houseNum, int flatNum) {
        this.addressId = addressId;
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseNum = houseNum;
        this.flatNum = flatNum;
    }

    public Address() {
        this.addressId = 0;
        this.country = null;
        this.city = null;
        this.street = null;
        this.houseNum = 0;
        this.flatNum = 0;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(int houseNum) {
        this.houseNum = houseNum;
    }

    public int getFlatNum() {
        return flatNum;
    }

    public void setFlatNum(int flatNum) {
        this.flatNum = flatNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return houseNum == address.houseNum &&
                flatNum == address.flatNum && Objects.equals(country, address.country) &&
                Objects.equals(city, address.city) && Objects.equals(street, address.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, street, houseNum, flatNum);
    }

    @Override
    public String toString() {
        return country + ", " + city + ", " + street + ", " + houseNum + (flatNum == 0 ? "" : "/" + flatNum);
    }
}
