package com.surya.rk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.surya.rk.entities.User;
import com.surya.rk.enums.UserRole;

import java.util.Optional;


@Repository
public interface UserRepository  extends JpaRepository<User, Long>
{

	 Optional<User> findFirstByEmail(String username);

	 Optional<User> findByUserRole(UserRole admin);

}
