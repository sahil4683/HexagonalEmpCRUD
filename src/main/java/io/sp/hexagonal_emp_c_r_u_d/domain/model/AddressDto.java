package io.sp.hexagonal_emp_c_r_u_d.domain.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AddressDto {

    @NotNull
    private Long id;

    @Size(max = 255)
    private String city;

    @Size(max = 255)
    private String country;

}
