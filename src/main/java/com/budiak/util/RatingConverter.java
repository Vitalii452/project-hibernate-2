package com.budiak.util;

import com.budiak.model.Film;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RatingConverter implements AttributeConverter<Film.Rating, String> {
    @Override
    public String convertToDatabaseColumn(Film.Rating attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public Film.Rating convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Film.Rating.valueOf(dbData);
    }
}
