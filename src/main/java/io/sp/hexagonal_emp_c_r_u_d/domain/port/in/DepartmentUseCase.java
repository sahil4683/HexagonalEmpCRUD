package io.sp.hexagonal_emp_c_r_u_d.domain.port.in;

import io.sp.hexagonal_emp_c_r_u_d.domain.model.DepartmentDto;
import jakarta.validation.Valid;

import java.util.List;

public interface DepartmentUseCase {

    List<DepartmentDto> findAll();

    DepartmentDto get(Long id);

    Long create(@Valid DepartmentDto departmentDTO);

    void update(Long id, @Valid DepartmentDto departmentDTO);

    void delete(Long id);
}

