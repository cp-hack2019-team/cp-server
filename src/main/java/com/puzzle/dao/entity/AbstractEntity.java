package com.puzzle.dao.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.UUID;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author ibez
 * @since 2019-06-06
 */
@MappedSuperclass
@EqualsAndHashCode(of = "uuid")
public abstract class AbstractEntity implements Serializable {

    @Id
    @GenericGenerator(
        name = "sequenceGenerator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = "sequence_name", value = "sequences"),
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "1")
        }
    )
    @GeneratedValue(generator = "sequenceGenerator")
    @Getter
    @Setter
    protected Long id;

    @Column(nullable = false, unique = true)
    @Getter
    protected UUID uuid = UUID.randomUUID();
}
