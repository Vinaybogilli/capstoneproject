package com.vinay.wipro.exception;

public class ErrorMessage {

	private String message;
	private String status;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ErrorMessage() {
		super();
	}
	public ErrorMessage(String message, String status) {
		super();
		this.message = message;
		this.status = status;
	}
	
	
}