package com.puzzle.resource;

import javax.annotation.Nullable;
import java.time.ZonedDateTime;
import java.util.UUID;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.puzzle.dao.entity.MedicineSchedule;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author ibez
 * @since 2019-06-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@JsonInclude(value = NON_NULL)
public class RecipeResource {

    @Nullable
    private UUID id;

    private UUID medicineId;

    private String medicineName;

    private ZonedDateTime createdTime;

    private MedicineSchedule schedule;

    private int dose;

    private int days;

    private int stock;

    private int daysCovered;
}
