package com.lcwd.user.service.exceptions;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	// extra properties that you want to mange
	public ResourceNotFoundException() {
		super("Resource not found on server !!");
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

}
