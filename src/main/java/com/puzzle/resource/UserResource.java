package com.puzzle.resource;

import java.util.UUID;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author ibez
 * @since 2019-06-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@JsonInclude(value = NON_NULL)
public class UserResource {

    @JsonProperty("id")
    private UUID uuid;

    @Length(max = 255)
    private String firstName;

    @Length(max = 255)
    private String lastName;

    @Length(max = 255)
    private String email;
}
