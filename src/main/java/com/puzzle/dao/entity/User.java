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
 * @since 2019-06-06
 */
@Entity
@Table(name = "user_")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User extends AbstractEntity {

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false)
    private String email;
}
