package io.sp.hexagonal_emp_c_r_u_d.infrastructure.configuration;

import io.sp.hexagonal_emp_c_r_u_d.domain.port.in.EmployeeUseCase;
import io.sp.hexagonal_emp_c_r_u_d.domain.port.out.EmployeeRepositoryPort;
import io.sp.hexagonal_emp_c_r_u_d.domain.service.EmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public EmployeeUseCase employeeUseCase(EmployeeRepositoryPort employeeRepositoryPort){
        return new EmployeeService(employeeRepositoryPort);
    }

}
