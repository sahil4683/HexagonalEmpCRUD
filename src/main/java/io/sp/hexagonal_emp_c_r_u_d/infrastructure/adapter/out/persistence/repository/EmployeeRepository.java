package io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.repository;

import io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Override
    @Query("select distinct e from Employee e left join fetch e.addresses left join fetch e.department")
    List<Employee> findAll();

    @Override
    @Query("select e from Employee e left join fetch e.addresses left join fetch e.department where e.id = :id")
    Optional<Employee> findById(@Param("id") Long id);

    long countByDepartment_Id(Long departmentId);
}
