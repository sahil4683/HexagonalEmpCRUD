package io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.repository;

import io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.entity.Department;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

//    @EntityGraph(attributePaths = "employees")
    List<Department> findAll();

}

