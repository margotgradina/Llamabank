package com.dellama.bank.webapi.model.approval;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class OpenBankAccountTypeConverter implements AttributeConverter<OpenBankAccountType, String> {

    @Override
    public String convertToDatabaseColumn(OpenBankAccountType openBankAccountType) {
        if(openBankAccountType == null) {
            return null;
        }

        return openBankAccountType.getBankAccountType();
    }

    @Override
    public OpenBankAccountType convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }

        return Stream.of(OpenBankAccountType.values())
                .filter(e -> e.getBankAccountType().equals(s))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
