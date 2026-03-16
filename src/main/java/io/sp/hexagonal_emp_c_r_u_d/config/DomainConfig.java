package io.sp.hexagonal_emp_c_r_u_d.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
// Scan entity package used by JPA
@EntityScan("io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.entity")
// Enable JPA repositories in the persistence.repository package
@EnableJpaRepositories("io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.repository")
@EnableTransactionManagement
public class DomainConfig {
}
