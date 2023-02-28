package com.dellama.bank.webapi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private Integer houseNumber;
    private String houseNumberExtension;
    private String postalCode;
    private String city;
    private String region;
    private String country;

    /**
     * Creates a new Address. Do not use directly, but use the AddressBuilder instead to actually create and set the address.
     *
     * @param street
     * @param houseNumber
     * @param houseNumberExtension
     * @param postalCode
     * @param city
     * @param region
     * @param country
     */
    public Address(String street, Integer houseNumber, String houseNumberExtension, String postalCode, String city, String region, String country) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.houseNumberExtension = houseNumberExtension;
        this.postalCode = postalCode;
        this.city = city;
        this.region = region;
        this.country = country;
    }

    public Address() {
    }

    //region get/setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getHouseNumberExtension() {
        return houseNumberExtension;
    }

    public void setHouseNumberExtension(String houseNumberExtension) {
        this.houseNumberExtension = houseNumberExtension;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return houseNumber.equals(address.houseNumber) &&
                houseNumberExtension.equals(address.houseNumberExtension) &&
                postalCode.equals(address.postalCode) &&
                country.equals(address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(houseNumber, houseNumberExtension, postalCode, country);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", houseNumber=" + houseNumber +
                ", houseNumberExtension='" + houseNumberExtension + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}


