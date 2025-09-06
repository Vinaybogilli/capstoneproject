package com.surya.rk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.surya.rk.entities.Customer;
import com.surya.rk.enums.UserRole;

import java.util.Optional;


@Repository
public interface UserRepository  extends JpaRepository<Customer, Long>
{

	 Optional<Customer> findFirstByEmail(String username);

	 Optional<Customer> findByUserRole(UserRole admin);

}
