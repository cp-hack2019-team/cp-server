package com.puzzle.resource;

import java.time.ZonedDateTime;
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
public class TakenMedicineEventResource {

    private UUID id;

    private ZonedDateTime time;
}
