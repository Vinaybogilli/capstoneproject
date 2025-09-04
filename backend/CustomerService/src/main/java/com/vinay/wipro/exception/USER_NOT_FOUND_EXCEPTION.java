package com.vinay.wipro.exception;

public class USER_NOT_FOUND_EXCEPTION   extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public USER_NOT_FOUND_EXCEPTION (String message) {
		super(message);
	}
}
