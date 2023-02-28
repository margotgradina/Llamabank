package com.dellama.bank.webapi.dummydata;

import com.dellama.bank.webapi.model.Address;
import com.dellama.bank.webapi.model.Client;
import com.github.javafaker.Faker;


public final class ClientFactory implements AbstractFactory<Client> {
    private static AddressFactory addressFactory = new AddressFactory();

    @Override
    public Client create() {
        Faker faker = new Faker();

        String userName = faker.name().username();
        String password = faker.internet().password(5, 30);
        String name = faker.name().fullName();

        String email = faker.bothify("????##@gmail.com");
        String phoneNumber = faker.numerify("##########");
        String bsn = faker.random().nextInt(1, 5) > 3 ? faker.numerify("#########") : faker.numerify("########");

        Address address = addressFactory.create();

        return new Client(userName, password, name, email, phoneNumber, bsn, address);
    }
}
