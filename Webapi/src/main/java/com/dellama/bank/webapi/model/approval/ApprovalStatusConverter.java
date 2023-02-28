package com.dellama.bank.webapi.model.approval;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ApprovalStatusConverter implements AttributeConverter<ApprovalStatus, String> {
    @Override
    public String convertToDatabaseColumn(ApprovalStatus approvalStatus) {
        if (approvalStatus == null) {
            return null;
        }

        return approvalStatus.getStatusName();
    }

    @Override
    public ApprovalStatus convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }

        return Stream.of(ApprovalStatus.values())
                     .filter(e -> e.getStatusName().equalsIgnoreCase(s))
                     .findFirst()
                     .orElseThrow(IllegalArgumentException::new);
    }
}
