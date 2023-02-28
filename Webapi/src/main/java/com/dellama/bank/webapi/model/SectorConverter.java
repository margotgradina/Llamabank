package com.dellama.bank.webapi.model;
import com.dellama.bank.webapi.model.Sector;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class SectorConverter implements AttributeConverter<Sector, String> {

    @Override
    public String convertToDatabaseColumn(Sector sector) {
        if (sector == null) {
            return null;
        }

        return sector.getStatusName();
    }


    @Override
    public Sector convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return Stream.of(Sector.values())
                     .filter(e -> e.getStatusName().equals(s))
                     .findFirst()
                     .orElseThrow(IllegalArgumentException::new);
    }
}
