package io.sp.hexagonal_emp_c_r_u_d.domain.port.in;

import io.sp.hexagonal_emp_c_r_u_d.domain.model.EmployeeDto;
import jakarta.validation.Valid;

import java.util.List;

public interface EmployeeUseCase {

    List<EmployeeDto> findAll();

    EmployeeDto get(Long id);

    Long create(@Valid EmployeeDto employeeDTO);

    void update(Long id, @Valid EmployeeDto employeeDTO);

    void delete(Long id);
}
