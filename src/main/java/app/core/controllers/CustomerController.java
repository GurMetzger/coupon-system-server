package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Category;
import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.exceptions.CouponAlreadyOwnedException;
import app.core.exceptions.CouponNotFoundException;
import app.core.exceptions.CustomerNotFoundException;
import app.core.exceptions.InvalidArgumentException;
import app.core.exceptions.TokenValidationException;
import app.core.services.CustomerService;

@RestController
@CrossOrigin
@RequestMapping("/api/customer")
public class CustomerController extends ClientController {

	// Attribute
	@Autowired
	private CustomerService customerService;
	
	
	// Methods
	@PostMapping("/purchaseCoupon")
	public Coupon purchaseCoupon(@RequestHeader String token, 
			@RequestHeader(name = "id") int customerID, @RequestBody Coupon coupon) {
		try {
			validateToken(token);
			return customerService.purchaseCoupon(coupon, customerID);
		} catch (CouponAlreadyOwnedException | InvalidArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (CustomerNotFoundException | CouponNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@GetMapping("/getAllCoupons")
	public List<Coupon> getCustomerCoupons(@RequestHeader String token, @RequestHeader(name = "id") int customerID) {
		try {
			validateToken(token);
			return customerService.getCustomerCoupons(customerID);
		} catch (CustomerNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@GetMapping("/getAllCouponsByCategory")
	public List<Coupon> getCustomerCouponsByCategory(@RequestHeader String token, 
			@RequestHeader(name = "id") int customerID, @RequestParam Category category) {
		try {
			validateToken(token);
			return customerService.getCustomerCoupons(category, customerID);
		} catch (InvalidArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (CustomerNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@GetMapping("/getAllCouponsByMaxPrice")
	public List<Coupon> getCustomerCouponsByMaxPrice(@RequestHeader String token, 
			@RequestHeader(name = "id") int customerID, @RequestParam double maxPrice) {
		try {
			validateToken(token);
			return customerService.getCustomerCoupons(maxPrice, customerID);
		} catch (InvalidArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (CustomerNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@GetMapping("/getCustomerDetails")
	public Customer getCustomerDetails(@RequestHeader String token, @RequestHeader(name = "id") int customerID) {
		try {
			validateToken(token);
			return customerService.getCustomerDetails(customerID);
		} catch (CustomerNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
}
