package com.dellama.bank.webapi.dummydata;

import com.dellama.bank.webapi.model.Address;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public final class AddressFactory implements AbstractFactory<Address>{
    @Override
    public Address create() {
        Faker faker = new Faker();

        String street = faker.address().streetName();
        Integer number = Integer.parseInt(faker.address().buildingNumber());
        String extension = faker.random().nextInt(1, 5) < 4 ? faker.letterify("?").toUpperCase() : null;
        String postalCode = faker.address().zipCode();
        String city = faker.address().city();
        String region = faker.random().nextInt(1, 5) < 4 ? faker.address().state() : null;
        String country = faker.address().country();

        return new Address(street, number, extension, postalCode, city, region, country);
    }
}
