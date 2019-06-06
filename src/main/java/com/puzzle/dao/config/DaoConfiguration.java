package com.puzzle.dao.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author ibez
 * @since 2019-06-06
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.puzzle.dao.repository")
@EntityScan(basePackages = "com.puzzle.dao.entity")
public class DaoConfiguration {
}
