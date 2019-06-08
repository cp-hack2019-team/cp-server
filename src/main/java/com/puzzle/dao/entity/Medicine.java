package com.puzzle.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


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
public class Medicine extends AbstractEntity {

    @Column
    private String name;

    @Column
    private String description;
}
