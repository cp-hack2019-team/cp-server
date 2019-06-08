package com.puzzle.dao.repository;

import javax.annotation.Nonnull;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzle.dao.entity.MedicineSchedule;
import lombok.RequiredArgsConstructor;

/**
 * @author ibez
 * @since 2019-06-08
 */
@Converter
@RequiredArgsConstructor
public class MedicineScheduleConverter implements AttributeConverter<MedicineSchedule, String> {

    @Nonnull
    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(MedicineSchedule schedule) {
        try {
            return objectMapper.writeValueAsString(schedule);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to convert schedule to json", e);
        }
    }

    @Override
    public MedicineSchedule convertToEntityAttribute(String string) {
        try {
            return objectMapper.readValue(string, MedicineSchedule.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read schedule from json", e);
        }
    }
}
