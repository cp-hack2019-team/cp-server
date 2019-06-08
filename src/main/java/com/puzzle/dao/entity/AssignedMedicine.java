package com.puzzle.dao.entity;

import javax.persistence.*;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;


import com.puzzle.dao.repository.MedicineScheduleConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

/**
 * @author ibez
 * @since 2019-06-08
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssignedMedicine extends AbstractEntity {

    @ManyToOne
    @JoinColumn
    private User patient;

    @ManyToOne
    @JoinColumn
    private Medicine medicine;

    @Column
    @Convert(converter = MedicineScheduleConverter.class)
    private MedicineSchedule schedule;

    @Column
    private int dose;

    @OneToMany(cascade = CascadeType.ALL)
    private List<TakenMedicineEvent> events;


}
