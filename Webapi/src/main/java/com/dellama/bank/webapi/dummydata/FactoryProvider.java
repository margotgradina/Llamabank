package com.dellama.bank.webapi.dummydata;

public final class FactoryProvider {
    private FactoryProvider() {
    }

    public static AbstractFactory getFactory(FactoryType factoryType) {
        return switch (factoryType) {
            case CLIENT -> new ClientFactory();
        };
    }
}
