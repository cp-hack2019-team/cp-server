package com.puzzle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


import com.puzzle.dao.entity.User;
import com.puzzle.dao.repository.UserRepository;
import com.puzzle.resource.UserResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    @GetMapping
    public List<UserResource> getUsers() {
        log.info("getUsers()");
        return toResource(userRepository.findAll(), UserController::toResource);
    }

    @PostMapping
    public UserResource createUser(@RequestBody UserResource resource) {
        return toResource(userRepository.save(fromResource(resource)));
    }


    private static UserResource toResource(User user) {
        return new UserResource(user.getUuid(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    private static User fromResource(UserResource resource) {
        return new User(resource.getFirstName(), resource.getLastName(), resource.getEmail());
    }

    private <T, R> List<R> toResource(List<T> entities, Function<T, R> mapper) {
        return entities.stream()
            .map(mapper)
            .collect(Collectors.toList());
    }
}
