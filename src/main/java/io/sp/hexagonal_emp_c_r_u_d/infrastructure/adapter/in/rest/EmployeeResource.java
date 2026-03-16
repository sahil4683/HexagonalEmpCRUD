package io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.in.rest;

import io.sp.hexagonal_emp_c_r_u_d.domain.model.EmployeeDto;
import io.sp.hexagonal_emp_c_r_u_d.domain.port.in.EmployeeUseCase;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/employees", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeResource {

    private final EmployeeUseCase EmployeeUseCase;

    public EmployeeResource(EmployeeUseCase EmployeeUseCase) {
        this.EmployeeUseCase = EmployeeUseCase;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok(EmployeeUseCase.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(EmployeeUseCase.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createEmployee(@RequestBody @Valid final EmployeeDto employeeDTO) {
        final Long createdId = EmployeeUseCase.create(employeeDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateEmployee(@PathVariable(name = "id") final Long id,
                                               @RequestBody @Valid final EmployeeDto employeeDTO) {
        EmployeeUseCase.update(id, employeeDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteEmployee(@PathVariable(name = "id") final Long id) {
        EmployeeUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

}
