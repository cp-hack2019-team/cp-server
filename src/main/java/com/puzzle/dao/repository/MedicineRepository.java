package com.puzzle.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.util.UUID;


import com.puzzle.dao.entity.Medicine;

/**
 * @author ibez
 * @since 2019-06-08
 */
public interface MedicineRepository extends JpaRepository<Medicine, Long>, JpaSpecificationExecutor<Medicine> {

    Medicine findByUuid(UUID uuid);

    @Query("select m from Medicine m where m.name like ?1%")
    Page<Medicine> findByNamePrefixAndSort(String prefix, Pageable pageable);
}
