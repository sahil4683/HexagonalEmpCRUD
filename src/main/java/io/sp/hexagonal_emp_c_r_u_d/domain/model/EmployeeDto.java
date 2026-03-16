package io.sp.hexagonal_emp_c_r_u_d.domain.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class EmployeeDto {

    // changed from Integer to Long to match repository ids
    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String contactNumber;

    List<AddressDto> addresses = new ArrayList<>();

}
