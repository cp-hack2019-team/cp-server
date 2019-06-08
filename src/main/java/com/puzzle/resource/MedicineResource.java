package com.puzzle.resource;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


import java.util.UUID;


import com.fasterxml.jackson.annotation.JsonInclude;
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
public class MedicineResource {

    @NotNull
    private UUID id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;
}
