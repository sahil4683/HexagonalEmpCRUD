package io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.in.rest;

import io.sp.hexagonal_emp_c_r_u_d.domain.model.DepartmentDto;
import io.sp.hexagonal_emp_c_r_u_d.domain.port.in.DepartmentUseCase;
import io.sp.hexagonal_emp_c_r_u_d.domain.port.in.EmployeeUseCase;
import io.sp.hexagonal_emp_c_r_u_d.domain.model.EmployeeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/departments", produces = MediaType.APPLICATION_JSON_VALUE)
public class DepartmentController {

    private final DepartmentUseCase departmentUseCase;
    private final EmployeeUseCase employeeUseCase;

    public DepartmentController(DepartmentUseCase departmentUseCase, EmployeeUseCase employeeUseCase) {
        this.departmentUseCase = departmentUseCase;
        this.employeeUseCase = employeeUseCase;
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        return ResponseEntity.ok(departmentUseCase.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartment(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(departmentUseCase.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> createDepartment(@RequestBody @Valid final DepartmentDto dto) {
        final Long createdId = departmentUseCase.create(dto);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateDepartment(@PathVariable(name = "id") final Long id,
                                                 @RequestBody @Valid final DepartmentDto dto) {
        departmentUseCase.update(id, dto);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteDepartment(@PathVariable(name = "id") final Long id) {
        departmentUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    // list employees in a department by filtering via EmployeeUseCase
    @GetMapping("/{id}/employees")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByDepartment(@PathVariable(name = "id") final Long id) {
        List<EmployeeDto> employees = employeeUseCase.findAll().stream()
                .filter(e -> e.getDepartmentId() != null && e.getDepartmentId().equals(id))
                .collect(Collectors.toList());
        return ResponseEntity.ok(employees);
    }

}
