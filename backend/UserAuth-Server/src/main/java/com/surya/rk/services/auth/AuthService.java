package com.surya.rk.services.auth;

import com.surya.rk.dto.SignupRequest;
import com.surya.rk.dto.CustomerDto;

public interface AuthService 
{

	CustomerDto signupUser(SignupRequest signupRequest);
	boolean hasUserWithEmail(String email);
}
