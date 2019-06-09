package com.puzzle.dao.entity;

import java.time.LocalTime;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ibez
 * @since 2019-06-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineSchedule {

    private List<LocalTime> times;
}
