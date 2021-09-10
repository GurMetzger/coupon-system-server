package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Category;
import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.exceptions.CompanyNotFoundException;
import app.core.exceptions.CouponNotFoundException;
import app.core.exceptions.CouponTitleAlreadyExistsException;
import app.core.exceptions.DatabaseMalfunctionCompanyException;
import app.core.exceptions.InvalidArgumentException;
import app.core.exceptions.InvalidCouponDataException;
import app.core.exceptions.TokenValidationException;
import app.core.services.CompanyService;
import app.core.utils.CouponPayload;

@RestController
@CrossOrigin
@RequestMapping("/api/company")
public class CompanyController extends ClientController {

	// Attribute
	@Autowired
	private CompanyService companyService;
	
	
	// Methods
	@PostMapping("/addCoupon")
	public Coupon addCoupon(@RequestHeader String token, 
			@RequestHeader(name = "id") int companyID, @ModelAttribute CouponPayload couponPayload) {
		try {
			validateToken(token);
			return companyService.addCoupon(couponPayload, companyID);
		} catch (InvalidCouponDataException | CouponTitleAlreadyExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (CompanyNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (DatabaseMalfunctionCompanyException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@PutMapping("/updateCoupon")
	public Coupon updateCoupon(@RequestHeader String token, 
			@RequestHeader(name = "id") int companyID, @ModelAttribute CouponPayload couponPayload) {
		try {
			validateToken(token);
			return companyService.updateCoupon(couponPayload, companyID);
		} catch (InvalidCouponDataException | CouponTitleAlreadyExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (CouponNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@DeleteMapping("/deleteCoupon")
	public int deleteCoupon(@RequestHeader String token, 
			@RequestHeader(name = "id") int companyID, @RequestParam(name = "id") int couponID) {
		try {
			validateToken(token);
			return companyService.deleteCoupon(couponID, companyID);
		} catch (InvalidCouponDataException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (CouponNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@GetMapping("/getAllCoupons")
	public List<Coupon> getCompanyCoupons(@RequestHeader String token, @RequestHeader(name = "id") int companyID) {
		try {
			validateToken(token);
			return companyService.getCompanyCoupons(companyID);
		} catch (CompanyNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@GetMapping("/getAllCouponsByCategory")
	public List<Coupon> getCompanyCouponsByCategory(@RequestHeader String token, 
				@RequestHeader(name = "id") int companyID, @RequestParam Category category) {
		try {
			validateToken(token);
			return companyService.getCompanyCoupons(category, companyID);
		} catch (InvalidArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (CompanyNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@GetMapping("/getAllCouponsByMaxPrice")
	public List<Coupon> getCompanyCouponsByMaxPrice(@RequestHeader String token, 
				@RequestHeader(name = "id") int companyID, @RequestParam double maxPrice) {
		try {
			validateToken(token);
			return companyService.getCompanyCoupons(maxPrice, companyID);
		} catch (InvalidArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (CompanyNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@GetMapping("/getCompanyDetails")
	public Company getCompanyDetails(@RequestHeader String token, @RequestHeader(name = "id") int companyID) {
		try {
			validateToken(token);
			return companyService.getCompanyDetails(companyID);
		} catch (CompanyNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
}
