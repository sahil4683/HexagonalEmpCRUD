package io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.repository;

import io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.entity.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Override
    @EntityGraph(attributePaths = {"addresses"})
    List<Employee> findAll();

    @Override
    @EntityGraph(attributePaths = {"addresses"})
    Optional<Employee> findById(Long id);
}
