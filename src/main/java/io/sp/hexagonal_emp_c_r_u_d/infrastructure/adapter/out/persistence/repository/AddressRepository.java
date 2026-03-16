package io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.repository;

import io.sp.hexagonal_emp_c_r_u_d.infrastructure.adapter.out.persistence.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, Long> {
}
