package com.puzzle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;


import com.google.common.base.MoreObjects;
import com.puzzle.dao.entity.AssignedMedicine;
import com.puzzle.dao.entity.Medicine;
import com.puzzle.dao.entity.TakenMedicineEvent;
import com.puzzle.dao.entity.User;
import com.puzzle.dao.repository.AssignedMedicineRepository;
import com.puzzle.dao.repository.MedicineRepository;
import com.puzzle.dao.repository.UserRepository;
import com.puzzle.resource.RecipeResource;
import com.puzzle.resource.TakenMedicineEventResource;
import com.puzzle.resource.UserResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.puzzle.controller.ControllerUtils.toResourceList;
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
@CrossOrigin
public class UserController {

    @Nonnull
    private final UserRepository userRepository;

    @Nonnull
    private final MedicineRepository medicineRepository;

    @Nonnull
    private final AssignedMedicineRepository assignedMedicineRepository;

    @Nonnull
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/me")
    public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        Map<Object, Object> model = new HashMap<>();
        model.put("username", userDetails.getUsername());
        model.put("roles", userDetails.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(toList())
        );
        return ok(model);
    }

    @GetMapping
    public List<UserResource> getUsers() {
        log.info("getUsers()");
        return toResourceList(userRepository.findAll(), UserController::toResource);
    }

    @GetMapping("/{id}")
    public UserResource getUser(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable UUID id)
    {
        User user = validateCurrentUser(userDetails, id);
        return toResource(user);
    }

    @GetMapping("/{id}/doctors")
    public List<UserResource> getUserDoctors(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable UUID id)
    {
        User user = validateCurrentUser(userDetails, id);
        return toResourceList(new ArrayList<>(user.getDoctors()), UserController::toResource);
    }

    @GetMapping("/{id}/patients")
    public List<UserResource> getUserPatients(@AuthenticationPrincipal UserDetails userDetails,
                                              @PathVariable UUID id)
    {
        User user = validateCurrentUser(userDetails, id);
        return toResourceList(new ArrayList<>(user.getPatients()), UserController::toResource);
    }

    @PostMapping
    public UserResource createUser(@RequestBody UserResource resource) {
        resource.setPassword(passwordEncoder.encode(resource.getPassword()));
        return toResource(userRepository.save(fromResource(resource)));
    }

    @PostMapping("/assigndoctor")
    public ResponseEntity assignDoctor(@AuthenticationPrincipal UserDetails userDetails,
                                       @RequestParam(name = "doctorId") @Nonnull UUID doctorId)
    {
        User patient = userRepository.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("no user with login " + userDetails.getUsername()));
        User doctor = userRepository.findByUuid(doctorId)
            .orElseThrow(() -> new IllegalArgumentException("no doctor with uuid " + doctorId));

        patient.getDoctors().add(doctor);
        doctor.getPatients().add(patient);

        userRepository.save(patient);
        userRepository.save(doctor);

        log.info("Assigned doctor {} to patient {}", doctor.getLogin(), patient.getLogin());

        return ok().build();
    }

    @PostMapping("/{id}/recipe")
    public ResponseEntity assignRecipe(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable UUID id,
                                       @RequestBody RecipeResource resource)
    {
        User user = userRepository.findByUuid(id)
            .orElseThrow(() -> new IllegalArgumentException("no user with id " + id));
        if (user.getLogin().equals(userDetails.getUsername())) {
            log.info("Patient {} is assigning itself medicine {} ", user.getLogin(), resource);
        } else if (isDoctor(user, userDetails.getUsername())) {
            log.info("Doctor {} is assigning to patient {} medicine {} ",
                userDetails.getUsername(), user.getLogin(), resource);
        } else {
            throw new IllegalArgumentException(
                "User " + userDetails.getUsername() + " is not allowed to assign medicines to user " + user.getLogin());
        }

        Medicine medicine = medicineRepository.findByUuid(resource.getMedicineId())
            .orElseThrow(() -> new IllegalArgumentException("No medicine with id " + resource.getMedicineId()));

        AssignedMedicine assignedMedicine = new AssignedMedicine();
        assignedMedicine.setPatient(user);
        assignedMedicine.setMedicine(medicine);
        assignedMedicine.setDose(resource.getDose());
        assignedMedicine.setSchedule(resource.getSchedule());
        assignedMedicine.setStock(resource.getStock());
        assignedMedicine.setCreatedTime(MoreObjects.firstNonNull(resource.getCreatedTime(), ZonedDateTime.now()));
        assignedMedicine.setDays(resource.getDays());

        assignedMedicineRepository.save(assignedMedicine);

        return ok().build();
    }

    @GetMapping("/{id}/recipes")
    public List<RecipeResource> getAssignedMedicines(@AuthenticationPrincipal UserDetails userDetails,
                                                     @PathVariable UUID id)
    {
        User user = validateCurrentUser(userDetails, id);
        List<AssignedMedicine> assignedMedicines = assignedMedicineRepository.findByPatient(user);

        return toResourceList(assignedMedicines, this::toResource);
    }

    @PostMapping("/{id}/recipes/{recipeId}/events")
    public ResponseEntity saveAssignedMedicineEvent(@AuthenticationPrincipal UserDetails userDetails,
                                                    @PathVariable UUID id,
                                                    @PathVariable UUID recipeId,
                                                    @RequestBody TakenMedicineEventResource resource)
    {
        User user = validateCurrentUser(userDetails, id);
        AssignedMedicine assignedMedicine = assignedMedicineRepository.findByUuid(recipeId)
            .orElseThrow(() -> new IllegalArgumentException("No recipe with id " + recipeId));
        if (!assignedMedicine.getPatient().equals(user)) {
            throw new IllegalArgumentException("Assigned recipe " + recipeId
                                               + " is not for user " + user.getLogin());
        }

        log.info("Saving event {} to recipe {}", resource, assignedMedicine.getUuid());

        TakenMedicineEvent event = new TakenMedicineEvent();
        event.setAssignedMedicine(assignedMedicine);
        event.setTime(resource.getTime());
        assignedMedicine.getEvents().add(event);
        assignedMedicineRepository.save(assignedMedicine);

        return ok().build();
    }

    @PutMapping("/{id}/recipes/{recipeId}/stock")
    public RecipeResource updateAssignedMedicineStock(@AuthenticationPrincipal UserDetails userDetails,
                                                      @PathVariable UUID id,
                                                      @PathVariable UUID recipeId,
                                                      @RequestBody int stock)
    {
        User user = validateCurrentUserOrDoctor(userDetails, id);
        AssignedMedicine assignedMedicine = assignedMedicineRepository.findByUuid(recipeId)
            .orElseThrow(() -> new IllegalArgumentException("No assigned medicine with id " + recipeId));
        log.info("User {} is changing stock of assigned medicine {} for {} from {} to {}",
            userDetails.getUsername(), user.getLogin(), assignedMedicine.getUuid(), assignedMedicine.getStock(), stock);
        assignedMedicine.setStock(stock);
        return toResource(assignedMedicineRepository.save(assignedMedicine));
    }


    @GetMapping("/{id}/recipes/{recipeId}/events")
    public List<TakenMedicineEventResource> getAssignedMedicineEvents(@AuthenticationPrincipal UserDetails userDetails,
                                                                      @PathVariable UUID id,
                                                                      @PathVariable UUID recipeId)
    {
        User user = validateCurrentUser(userDetails, id);
        AssignedMedicine assignedMedicine = assignedMedicineRepository.findByUuid(recipeId)
            .orElseThrow(() -> new IllegalArgumentException("No recipe with id " + recipeId));
        if (!assignedMedicine.getPatient().equals(user)) {
            throw new IllegalArgumentException("Recipe " + recipeId
                                               + " is not for user " + user.getLogin());
        }

        return toResourceList(assignedMedicine.getEvents(), this::toResource);
    }

    private User validateCurrentUser(@AuthenticationPrincipal UserDetails userDetails,
                                     @PathVariable UUID id)
    {
        User user = userRepository.findByUuid(id)
            .orElseThrow(() -> new IllegalArgumentException("no user with id " + id));
        if (!user.getLogin().equals(userDetails.getUsername())) {
            throw new IllegalArgumentException(
                "User " + userDetails.getUsername() + " is not allowed to see user " + user.getLogin());
        }
        return user;
    }

    private User validateCurrentUserOrDoctor(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable UUID id)
    {
        User user = userRepository.findByUuid(id)
            .orElseThrow(() -> new IllegalArgumentException("no user with id " + id));
        if (!user.getLogin().equals(userDetails.getUsername())
            || !isDoctor(user, userDetails.getUsername()))
        {
            throw new IllegalArgumentException("Only user itself or doctor is allowed to do it");
        }
        return user;
    }

    private boolean isDoctor(User user, String candidateLogin) {
        return user.getDoctors().stream().anyMatch(doctor -> doctor.getLogin().equals(candidateLogin));
    }

    private static UserResource toResource(User user) {
        return new UserResource(user.getUuid(), user.getLogin(), "",
            user.getFirstName(), user.getLastName(),
            user.getBirthDate().toLocalDate(), user.getEmail(), user.getPhoneNumber());
        //user.getPatients().stream().map(User::getUuid).collect(Collectors.toSet()),
        //user.getDoctors().stream().map(User::getUuid).collect(Collectors.toSet()));
    }

    private static User fromResource(UserResource resource) {
        return new User(resource.getLogin(), resource.getPassword(),
            resource.getFirstName(), resource.getLastName(),
            LocalDateTime.of(resource.getBirthDate(), LocalTime.of(0, 0)),
            resource.getEmail(), resource.getPhoneNumber(),
            Collections.emptySet(), Collections.emptySet()); // TODO
    }

    private RecipeResource toResource(AssignedMedicine assignedMedicine) {
        return new RecipeResource(assignedMedicine.getUuid(), assignedMedicine.getUuid(),
            assignedMedicine.getMedicine().getName(), assignedMedicine.getCreatedTime(),
            assignedMedicine.getSchedule(), assignedMedicine.getDose(),
            assignedMedicine.getDays(), assignedMedicine.getStock());
    }

    private TakenMedicineEventResource toResource(TakenMedicineEvent takenMedicineEvent) {
        return new TakenMedicineEventResource(takenMedicineEvent.getUuid(), takenMedicineEvent.getTime());
    }
}
