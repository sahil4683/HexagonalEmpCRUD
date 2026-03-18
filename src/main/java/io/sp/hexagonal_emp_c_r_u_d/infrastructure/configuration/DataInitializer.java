package io.sp.hexagonal_emp_c_r_u_d.infrastructure.configuration;

import io.sp.hexagonal_emp_c_r_u_d.domain.model.AddressDto;
import io.sp.hexagonal_emp_c_r_u_d.domain.model.EmployeeDto;
import io.sp.hexagonal_emp_c_r_u_d.domain.model.DepartmentDto;
import io.sp.hexagonal_emp_c_r_u_d.domain.port.in.EmployeeUseCase;
import io.sp.hexagonal_emp_c_r_u_d.domain.port.in.DepartmentUseCase;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EmployeeUseCase employeeUseCase;
    private final DepartmentUseCase departmentUseCase;

    public DataInitializer(EmployeeUseCase employeeUseCase, DepartmentUseCase departmentUseCase) {
        this.employeeUseCase = employeeUseCase;
        this.departmentUseCase = departmentUseCase;
    }

    @Override
    public void run(String... args) throws Exception {
        // only insert if no employees exist
        if (!employeeUseCase.findAll().isEmpty()) {
            return;
        }

        // create some departments
        DepartmentDto eng = new DepartmentDto(); eng.setName("Engineering");
        Long engId = departmentUseCase.create(eng);
        DepartmentDto hr = new DepartmentDto(); hr.setName("HR");
        Long hrId = departmentUseCase.create(hr);

        // data from user
        List<EmployeeDto> seed = new ArrayList<>();

        EmployeeDto e1 = new EmployeeDto();
        e1.setName("Sahil");
        e1.setContactNumber("32532");
        e1.setDepartmentId(engId);
        List<AddressDto> a1 = new ArrayList<>();
        AddressDto a11 = new AddressDto(); a11.setId(null); a11.setCity("Mumbai"); a11.setCountry("IND"); a1.add(a11);
        AddressDto a12 = new AddressDto(); a12.setId(null); a12.setCity("Pune"); a12.setCountry("IND"); a1.add(a12);
        e1.setAddresses(a1);
        seed.add(e1);

        EmployeeDto e2 = new EmployeeDto();
        e2.setName("Rohan");
        e2.setContactNumber("9876543210");
        e2.setDepartmentId(engId);
        List<AddressDto> a2 = new ArrayList<>();
        AddressDto a21 = new AddressDto(); a21.setId(null); a21.setCity("Delhi"); a21.setCountry("IND"); a2.add(a21);
        AddressDto a22 = new AddressDto(); a22.setId(null); a22.setCity("Noida"); a22.setCountry("IND"); a2.add(a22);
        e2.setAddresses(a2);
        seed.add(e2);

        EmployeeDto e3 = new EmployeeDto();
        e3.setName("Amit");
        e3.setContactNumber("9123456780");
        e3.setDepartmentId(hrId);
        List<AddressDto> a3 = new ArrayList<>();
        AddressDto a31 = new AddressDto(); a31.setId(null); a31.setCity("Bangalore"); a31.setCountry("IND"); a3.add(a31);
        AddressDto a32 = new AddressDto(); a32.setId(null); a32.setCity("Mysore"); a32.setCountry("IND"); a3.add(a32);
        e3.setAddresses(a3);
        seed.add(e3);

        EmployeeDto e4 = new EmployeeDto();
        e4.setName("Neha");
        e4.setContactNumber("9988776655");
        e4.setDepartmentId(hrId);
        List<AddressDto> a4 = new ArrayList<>();
        AddressDto a41 = new AddressDto(); a41.setId(null); a41.setCity("Hyderabad"); a41.setCountry("IND"); a4.add(a41);
        AddressDto a42 = new AddressDto(); a42.setId(null); a42.setCity("Secunderabad"); a42.setCountry("IND"); a4.add(a42);
        e4.setAddresses(a4);
        seed.add(e4);

        EmployeeDto e5 = new EmployeeDto();
        e5.setName("Priya");
        e5.setContactNumber("9012345678");
        // leave Priya without department
        List<AddressDto> a5 = new ArrayList<>();
        AddressDto a51 = new AddressDto(); a51.setId(null); a51.setCity("Chennai"); a51.setCountry("IND"); a5.add(a51);
        AddressDto a52 = new AddressDto(); a52.setId(null); a52.setCity("Coimbatore"); a52.setCountry("IND"); a5.add(a52);
        e5.setAddresses(a5);
        seed.add(e5);

        for (EmployeeDto emp : seed) {
            employeeUseCase.create(emp);
        }
    }
}
