package io.sp.hexagonal_emp_c_r_u_d.domain.port.out;

import io.sp.hexagonal_emp_c_r_u_d.domain.model.EmployeeDto;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepositoryPort {

    List<EmployeeDto> findAll();

    Optional<EmployeeDto> findById(Long id);

    Long create(EmployeeDto employeeDTO);

    void update(Long id, EmployeeDto employeeDTO);

    void delete(Long id);
}
