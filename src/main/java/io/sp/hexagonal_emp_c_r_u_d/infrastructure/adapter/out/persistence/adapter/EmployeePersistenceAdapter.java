package io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.adapter;

import io.sp.hexagonal_emp_c_r_u_d.domain.model.AddressDto;
import io.sp.hexagonal_emp_c_r_u_d.domain.model.EmployeeDto;
import io.sp.hexagonal_emp_c_r_u_d.domain.port.out.EmployeeRepositoryPort;
import io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.entity.Address;
import io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.entity.Employee;
import io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.repository.EmployeeRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EmployeePersistenceAdapter implements EmployeeRepositoryPort {

    private final EmployeeRepository employeeRepository;

    public EmployeePersistenceAdapter(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<EmployeeDto> findAll() {
        return employeeRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<EmployeeDto> findById(Long id) {
        return employeeRepository.findById(id).map(this::toDto);
    }

    @Override
    public Long create(EmployeeDto employeeDTO) {
        Employee entity = toEntity(employeeDTO);
        // ensure id is null for create
        entity.setId(null);
        // ensure bi-directional links
        if (entity.getAddresses() != null) {
            entity.getAddresses().forEach(a -> a.setEmployee(entity));
        }
        Employee saved = employeeRepository.save(entity);
        return saved.getId();
    }

    @Override
    public void update(Long id, EmployeeDto employeeDTO) {
        Optional<Employee> existing = employeeRepository.findById(id);
        if (existing.isPresent()) {
            Employee entity = existing.get();
            entity.setName(employeeDTO.getName());
            entity.setContactNumber(employeeDTO.getContactNumber());

            // sync addresses: simple strategy - clear and replace
            entity.getAddresses().clear();
            if (employeeDTO.getAddresses() != null) {
                for (AddressDto adto : employeeDTO.getAddresses()) {
                    Address addr = new Address();
                    addr.setId(adto.getId());
                    addr.setCity(adto.getCity());
                    addr.setCountry(adto.getCountry());
                    addr.setEmployee(entity);
                    entity.getAddresses().add(addr);
                }
            }

            employeeRepository.save(entity);
        }
    }

    @Override
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    private EmployeeDto toDto(Employee e) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setContactNumber(e.getContactNumber());
        if (e.getAddresses() != null) {
            List<AddressDto> ads = e.getAddresses().stream().map(a -> {
                AddressDto adto = new AddressDto();
                adto.setId(a.getId());
                adto.setCity(a.getCity());
                adto.setCountry(a.getCountry());
                return adto;
            }).collect(Collectors.toList());
            dto.setAddresses(ads);
        }
        return dto;
    }

    private Employee toEntity(EmployeeDto dto) {
        Employee e = new Employee();
        e.setId(dto.getId());
        e.setName(dto.getName());
        e.setContactNumber(dto.getContactNumber());

        List<Address> addresses = new ArrayList<>();
        if (dto.getAddresses() != null) {
            for (AddressDto adto : dto.getAddresses()) {
                Address a = new Address();
                a.setId(adto.getId());
                a.setCity(adto.getCity());
                a.setCountry(adto.getCountry());
                a.setEmployee(e); // set back-reference
                addresses.add(a);
            }
        }
        e.setAddresses(addresses);
        return e;
    }
}
