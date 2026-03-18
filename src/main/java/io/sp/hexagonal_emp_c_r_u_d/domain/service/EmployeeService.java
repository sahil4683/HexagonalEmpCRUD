package io.sp.hexagonal_emp_c_r_u_d.domain.service;

import io.sp.hexagonal_emp_c_r_u_d.domain.model.EmployeeDto;
import io.sp.hexagonal_emp_c_r_u_d.domain.port.in.EmployeeUseCase;
import io.sp.hexagonal_emp_c_r_u_d.domain.port.out.EmployeeRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmployeeService implements EmployeeUseCase {

    private final EmployeeRepositoryPort repositoryPort;

    public EmployeeService(EmployeeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public List<EmployeeDto> findAll() {
        return repositoryPort.findAll();
    }

    @Override
    public EmployeeDto get(Long id) {
        Optional<EmployeeDto> dto = repositoryPort.findById(id);
        return dto.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found: " + id));
    }

    @Override
    public Long create(EmployeeDto employeeDTO) {
        return repositoryPort.create(employeeDTO);
    }

    @Override
    public void update(Long id, EmployeeDto employeeDTO) {
        repositoryPort.update(id, employeeDTO);
    }

    @Override
    public void delete(Long id) {
        repositoryPort.delete(id);
    }
}
