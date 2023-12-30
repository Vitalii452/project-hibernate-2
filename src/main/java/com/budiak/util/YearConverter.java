package com.budiak.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;

@Converter
public class YearConverter implements AttributeConverter<Integer, String> {

    @Override
    public String convertToDatabaseColumn(Integer attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toString();
    }

    @Override
    public Integer convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        LocalDate date = LocalDate.parse(dbData);
        return date.getYear();
    }
}

