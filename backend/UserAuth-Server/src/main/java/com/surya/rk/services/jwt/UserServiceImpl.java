package com.surya.rk.services.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.surya.rk.repositories.UserRepository;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	
	private final UserRepository userRepository;

	@Override
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return userRepository.findFirstByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User Not Found!"));
			}
		};
	}

}
