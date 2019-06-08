package com.puzzle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Nonnull;


import java.util.List;
import java.util.UUID;


import com.puzzle.dao.entity.Medicine;
import com.puzzle.dao.repository.MedicineRepository;
import com.puzzle.resource.MedicineResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.puzzle.controller.ControllerUtils.toResourceList;

/**
 * @author ibez
 * @since 2019-06-08
 */
@RestController
@RequestMapping("/medicines")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
@CrossOrigin
public class MedicineController {

    private final MedicineRepository medicineRepository;

    @GetMapping
    public Page<MedicineResource> getMedicines(@RequestParam(name = "prefix", defaultValue = "") @Nonnull String prefix,
                                       Pageable pageable) {
        Page<Medicine> page = medicineRepository.findByNamePrefixAndSort(prefix, pageable);
        List<MedicineResource> resources = toResourceList(page.getContent(), MedicineController::toResource);
        return new PageImpl<>(
            resources,
            PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()),
            page.getTotalElements());
    }

    @GetMapping("/{id}")
    public MedicineResource getMedicine(@PathVariable @Nonnull UUID id) {
        return toResource(medicineRepository.findByUuid(id)
            .orElseThrow(() -> new IllegalArgumentException("No medicine with id " + id)));
    }

    @PostMapping
    public ResponseEntity<Medicine> addMedicine(@RequestBody @Nonnull MedicineResource resource) {
        return new ResponseEntity<>(
            medicineRepository.save(fromResource(resource)), HttpStatus.CREATED);
    }

    private static Medicine fromResource(MedicineResource resource) {
        return new Medicine(resource.getName(), resource.getDescription(), resource.getImageUrl());
    }

    private static MedicineResource toResource(Medicine medicine) {
        return new MedicineResource(medicine.getUuid(), medicine.getName(), medicine.getDescription(),
            medicine.getImageUrl());
    }
}
