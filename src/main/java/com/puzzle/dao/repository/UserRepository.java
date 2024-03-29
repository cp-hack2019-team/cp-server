package com.puzzle.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;


import com.puzzle.dao.entity.User;

/**
 * @author ibez
 * @since 2019-06-06
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByLogin(String login);
}
