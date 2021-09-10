package app.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exceptions.CompanyEmailAlreadyExistsException;
import app.core.exceptions.CompanyNameAlreadyExistsException;
import app.core.exceptions.CompanyNotFoundException;
import app.core.exceptions.CredentialsException;
import app.core.exceptions.CustomerEmailAlreadyExistsException;
import app.core.exceptions.CustomerNotFoundException;
import app.core.exceptions.DatabaseMalfunctionAdminException;
import app.core.exceptions.InvalidCompanyDataException;
import app.core.exceptions.InvalidCustomerDataException;
import app.core.exceptions.LoginFailedException;
import app.core.login.ClientDetails;
import app.core.login.Credentials;
import app.core.services.AuthService;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

	// Attribute
	@Autowired
	private AuthService authService;
	
	
	// Methods
	@PostMapping("/login/admin")
	public ClientDetails loginAdmin(@RequestBody Credentials credentials) {		
		try {
			return authService.loginAdmin(credentials);
		} catch (LoginFailedException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		} catch (CredentialsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/login/company")
	public ClientDetails loginCompany(@RequestBody Credentials credentials) {		
		try {
			return authService.loginCompany(credentials);
		} catch (LoginFailedException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		} catch (CredentialsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (CompanyNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PostMapping("/login/customer")
	public ClientDetails loginCustomer(@RequestBody Credentials credentials) {
		try {
			return authService.loginCustomer(credentials);
		} catch (LoginFailedException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		} catch (CredentialsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (CustomerNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@PostMapping("/register/company")
	public ClientDetails registerCompany(@RequestBody Company company) {
		try {
			return authService.registerCompany(company);
		} catch (InvalidCompanyDataException | CompanyNameAlreadyExistsException | CompanyEmailAlreadyExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (DatabaseMalfunctionAdminException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	@PostMapping("/register/customer")
	public ClientDetails registerCustomer(@RequestBody Customer customer) {
		try {
			return authService.registerCustomer(customer);
		} catch (InvalidCustomerDataException | CustomerEmailAlreadyExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (DatabaseMalfunctionAdminException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
}
