package app.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import app.core.exceptions.TokenValidationException;
import app.core.utils.JwtUtil;

public abstract class ClientController {
	
	// Attribute
	@Autowired
	protected JwtUtil jwtUtil;
	
	
	// Methods
	protected void validateToken(String token) throws TokenValidationException {
		try {
			jwtUtil.validateToken(token);
		} catch (TokenValidationException e) {
			throw new TokenValidationException(e.getMessage(), e);
		}
	}
	
}
