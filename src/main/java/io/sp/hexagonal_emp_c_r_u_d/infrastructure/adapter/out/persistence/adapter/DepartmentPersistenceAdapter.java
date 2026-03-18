package io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.adapter;

import io.sp.hexagonal_emp_c_r_u_d.domain.model.DepartmentDto;
import io.sp.hexagonal_emp_c_r_u_d.domain.port.out.DepartmentRepositoryPort;
import io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.entity.Department;
import io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.repository.EmployeeRepository;
import io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.repository.DepartmentRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Component
public class DepartmentPersistenceAdapter implements DepartmentRepositoryPort {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentPersistenceAdapter(DepartmentRepository departmentRepository,
                                        EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<DepartmentDto> findAll() {
        return departmentRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<DepartmentDto> findById(Long id) {
        return departmentRepository.findById(id).map(this::toDto);
    }

    @Override
    public Long create(DepartmentDto departmentDTO) {
        Department entity = toEntity(departmentDTO);
        entity.setId(null);
        Department saved = departmentRepository.save(entity);
        return saved.getId();
    }

    @Override
    public void update(Long id, DepartmentDto departmentDTO) {
        Department entity = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found: " + id));
        entity.setName(departmentDTO.getName());
        departmentRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found: " + id);
        }
        if (employeeRepository.countByDepartment_Id(id) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Department " + id + " still has employees assigned");
        }
        departmentRepository.deleteById(id);
    }

    private DepartmentDto toDto(Department d) {
        DepartmentDto dto = new DepartmentDto();
        dto.setId(d.getId());
        dto.setName(d.getName());
        return dto;
    }

    private Department toEntity(DepartmentDto dto) {
        Department d = new Department();
        d.setId(dto.getId());
        d.setName(dto.getName());
        return d;
    }
}

