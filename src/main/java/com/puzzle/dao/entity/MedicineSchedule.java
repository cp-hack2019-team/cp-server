package com.puzzle.dao.entity;

import java.time.LocalTime;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ibez
 * @since 2019-06-08
 */
@Data
@AllArgsConstructor
public class MedicineSchedule {

    private List<LocalTime> times;
}
