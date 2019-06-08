package com.puzzle.resource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
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
    private String login;

    @Length(max = 255)
    private String password;

    @Length(max = 255)
    private String firstName;

    @Length(max = 255)
    private String lastName;

    @Length
    private LocalDate birthDate;

    @Length(max = 255)
    private String email;

    @Length // TODO validate phone number
    private String phoneNumber;

//    private Set<UUID> patients;
//
//    private Set<UUID> doctors;
}
