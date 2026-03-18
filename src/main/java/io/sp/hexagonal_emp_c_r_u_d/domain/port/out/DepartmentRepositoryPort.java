package io.sp.hexagonal_emp_c_r_u_d.domain.port.out;

import io.sp.hexagonal_emp_c_r_u_d.domain.model.DepartmentDto;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepositoryPort {

    List<DepartmentDto> findAll();

    Optional<DepartmentDto> findById(Long id);

    Long create(DepartmentDto departmentDTO);

    void update(Long id, DepartmentDto departmentDTO);

    void delete(Long id);

}

