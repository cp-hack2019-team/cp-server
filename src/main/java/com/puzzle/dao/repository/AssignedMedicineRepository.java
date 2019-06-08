package com.puzzle.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


import com.puzzle.dao.entity.AssignedMedicine;
import com.puzzle.dao.entity.User;

/**
 * @author ibez
 * @since 2019-06-08
 */
public interface AssignedMedicineRepository extends JpaRepository<AssignedMedicine, Long>, JpaSpecificationExecutor<AssignedMedicine> {

    List<AssignedMedicine> findByPatient(User patient);
}
