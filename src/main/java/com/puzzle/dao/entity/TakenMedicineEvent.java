package com.puzzle.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


import java.time.ZonedDateTime;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ibez
 * @since 2019-06-08
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TakenMedicineEvent extends AbstractEntity {

    @ManyToOne
    @JoinColumn
    private AssignedMedicine assignedMedicine;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime time;
}
