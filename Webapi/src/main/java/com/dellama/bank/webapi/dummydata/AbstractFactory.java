package com.dellama.bank.webapi.dummydata;

import java.util.ArrayList;
import java.util.List;

public sealed interface AbstractFactory<T>
        permits
        AddressFactory,
        ClientFactory {
    T create();

    default List<T> create(int count) {
        List<T> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            items.add(create());
            System.out.println(i);
        }
        return items;
    }
}
