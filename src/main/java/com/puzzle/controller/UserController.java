package com.puzzle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


import com.puzzle.dao.entity.User;
import com.puzzle.dao.repository.UserRepository;
import com.puzzle.resource.UserResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author ibez
 * @since 2019-06-06
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class UserController {

    @Nonnull
    private final UserRepository userRepository;

    @Nonnull
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/me")
    public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        Map<Object, Object> model = new HashMap<>();
        model.put("username", userDetails.getUsername());
        model.put("roles", userDetails.getAuthorities()
            .stream()
            .map(a -> a.getAuthority())
            .collect(toList())
        );
        return ok(model);
    }

    @GetMapping
    public List<UserResource> getUsers() {
        log.info("getUsers()");
        return toResource(userRepository.findAll(), UserController::toResource);
    }

    @PostMapping
    public UserResource createUser(@RequestBody UserResource resource) {
        resource.setPassword(passwordEncoder.encode(resource.getPassword()));
        return toResource(userRepository.save(fromResource(resource)));
    }


    private static UserResource toResource(User user) {
        return new UserResource(user.getUuid(), user.getLogin(), "",
            user.getFirstName(), user.getLastName(),
            user.getBirthDate().toLocalDate(), user.getEmail(), user.getPhoneNumber(),
            Collections.emptySet(), //TODO
            Collections.emptySet()); //TODO
    }

    private static User fromResource(UserResource resource) {
        return new User(resource.getLogin(), resource.getPassword(),
            resource.getFirstName(), resource.getLastName(),
            LocalDateTime.of(resource.getBirthDate(), LocalTime.of(0, 0)),
            resource.getEmail(), resource.getPhoneNumber(),
            Collections.emptyList(), Collections.emptyList()); // TODO
    }

    private <T, R> List<R> toResource(List<T> entities, Function<T, R> mapper) {
        return entities.stream()
            .map(mapper)
            .collect(toList());
    }
}
