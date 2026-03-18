package io.sp.hexagonal_emp_c_r_u_d.domain.service;

import io.sp.hexagonal_emp_c_r_u_d.domain.model.DepartmentDto;
import io.sp.hexagonal_emp_c_r_u_d.domain.port.in.DepartmentUseCase;
import io.sp.hexagonal_emp_c_r_u_d.domain.port.out.DepartmentRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DepartmentService implements DepartmentUseCase {

    private final DepartmentRepositoryPort repositoryPort;

    public DepartmentService(DepartmentRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public List<DepartmentDto> findAll() {
        return repositoryPort.findAll();
    }

    @Override
    public DepartmentDto get(Long id) {
        Optional<DepartmentDto> dto = repositoryPort.findById(id);
        return dto.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found: " + id));
    }

    @Override
    public Long create(DepartmentDto departmentDTO) {
        return repositoryPort.create(departmentDTO);
    }

    @Override
    public void update(Long id, DepartmentDto departmentDTO) {
        repositoryPort.update(id, departmentDTO);
    }

    @Override
    public void delete(Long id) {
        repositoryPort.delete(id);
    }
}

