package com.dellama.bank.webapi.DTO;

import javax.validation.constraints.*;

/**
 * Data Transfer Object class for creating a new client
 */
public class NewClientDTO {
    @NotBlank(message = "username is mandatory")
    @Size(min = 5, max = 30, message = "username should be between 5 and 30 characters")
    protected String userName;

    @NotBlank(message = "password is mandatory")
    @Size(min = 5, max = 30, message = "password should be between 5 and 30 characters")
    protected String password;

    @NotBlank(message = "name is mandatory")
    protected String name;

    @NotBlank(message = "email is mandatory")
    @Email
    private String email;

    @Pattern(regexp = "^(?:0|(?:\\+|00) ?31 ?)(?:(?:[1-9] ?(?:[0-9] ?){8})|(?:6 ?-? ?[1-9] ?(?:[0-9] ?){7})|(?:[1,2,3,4,5,7,8,9]\\d ?-? ?[1-9] ?(?:[0-9] ?){6})|(?:[1,2,3,4,5,7,8,9]\\d{2} ?-? ?[1-9] ?(?:[0-9] ?){5}))$",
            message = "phone number not valid")
    private String phoneNumber;

    @NotBlank(message = "bsn is mandatory")
    @Size(min = 8, max = 9, message = "BSN should be 8 or 9 characters")
    private String bsn;

    @NotBlank(message = "street is mandatory")
    private String street;

    @NotNull(message = "housenumber is mandatory")
    private Integer houseNumber;
    private String houseNumberExtension;

    @NotBlank(message = "postal code is mandatory")
    @Pattern(regexp = "(?i)^[a-z0-9][a-z0-9\\- ]{0,10}[a-z0-9]$", message = "postalcode not valid")
    private String postalCode;

    @NotBlank(message = "city is mandatory")
    private String city;
    private String region;

    @NotBlank(message = "country is mandatory")
    private String country;

    public NewClientDTO() {
    }

    //region get/setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBsn() {
        return bsn;
    }

    public void setBsn(String bsn) {
        this.bsn = bsn;
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
    public String toString() {
        return "NewClientDTO{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", BSN='" + bsn + '\'' +
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
