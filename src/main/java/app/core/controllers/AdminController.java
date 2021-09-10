package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.exceptions.CompanyEmailAlreadyExistsException;
import app.core.exceptions.CompanyNameAlreadyExistsException;
import app.core.exceptions.CompanyNameUpdateException;
import app.core.exceptions.CompanyNotFoundException;
import app.core.exceptions.CouponNotFoundException;
import app.core.exceptions.CustomerEmailAlreadyExistsException;
import app.core.exceptions.CustomerNotFoundException;
import app.core.exceptions.DatabaseMalfunctionAdminException;
import app.core.exceptions.InvalidCompanyDataException;
import app.core.exceptions.InvalidCustomerDataException;
import app.core.exceptions.TokenValidationException;
import app.core.services.AdminService;
import app.core.utils.ImageUtil;

@RestController
@CrossOrigin
@RequestMapping("/api/admin")
public class AdminController extends ClientController {	
	
	// Attribute
	@Autowired
	private AdminService adminService;
	@Autowired
	private ImageUtil imageUtil;
	
	
	// Methods
	@GetMapping("/getAllCoupons") // No Token required, all users use this
	public List<Coupon> getAllCoupons() {
		return adminService.getAllCoupons();
	}
	
	@GetMapping("/getOneCoupon/{id}") // No Token required, all users use this
	public Coupon getOneCoupon(@PathVariable(name = "id") int couponID) {
		try {
			return adminService.getOneCoupon(couponID);
		} catch (CouponNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@GetMapping("/getOneCouponImage/{imageName}") // No Token required, all users use this
	public ByteArrayResource getOneImage(@PathVariable String imageName) {
		try {
			return imageUtil.getImageInputStream(imageName);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	@PostMapping("/addCompany")
	public Company addCompany(@RequestHeader String token, @RequestBody Company company) {
		try {
			validateToken(token);
			return adminService.addCompany(company);
		} catch (InvalidCompanyDataException | CompanyNameAlreadyExistsException | CompanyEmailAlreadyExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (DatabaseMalfunctionAdminException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}	
	}
	
	@PutMapping("/updateCompany")
	public Company updateCompany(@RequestHeader String token, @RequestBody Company company) {
		try {
			validateToken(token);
			return adminService.updateCompany(company);
		} catch (InvalidCompanyDataException | CompanyEmailAlreadyExistsException | CompanyNameUpdateException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (CompanyNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@DeleteMapping("/deleteCompany")
	public int deleteCompany(@RequestHeader String token, @RequestParam(name = "id") int companyID) {
		try {
			validateToken(token);
			return adminService.deleteCompany(companyID);
		} catch (CompanyNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@GetMapping("/getAllCompanies")
	public List<Company> getAllCompanies(@RequestHeader String token) {
		try {
			validateToken(token);
			return adminService.getAllCompanies();
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@GetMapping("/getOneCompany/{id}")
	public Company getOneCompany(@RequestHeader String token, @PathVariable(name = "id") int companyID) {
		try {
			validateToken(token);
			return adminService.getOneCompany(companyID);
		} catch (CompanyNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@PostMapping("/addCustomer")
	public Customer addCustomer(@RequestHeader String token, @RequestBody Customer customer) {
		try {
			validateToken(token);
			return adminService.addCustomer(customer);
		} catch (InvalidCustomerDataException | CustomerEmailAlreadyExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (DatabaseMalfunctionAdminException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@PutMapping("/updateCustomer")
	public Customer updateCustomer(@RequestHeader String token, @RequestBody Customer customer) {
		try {
			validateToken(token);
			return adminService.updateCustomer(customer);
		} catch (InvalidCustomerDataException | CustomerEmailAlreadyExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (CustomerNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@DeleteMapping("/deleteCustomer")
	public int deleteCustomer(@RequestHeader String token, @RequestParam(name = "id") int customerID) {
		try {
			validateToken(token);
			return adminService.deleteCustomer(customerID);
		} catch (CustomerNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@GetMapping("/getAllCustomers")
	public List<Customer> getAllCustomers(@RequestHeader String token) {
		try {
			validateToken(token);
			return adminService.getAllCustomers();	
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@GetMapping("/getOneCustomer/{id}")
	public Customer getOneCustomer(@RequestHeader String token, @PathVariable(name = "id") int customerID) {
		try {
			validateToken(token);
			return adminService.getOneCustomer(customerID);
		} catch (CustomerNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (TokenValidationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
}
