package app.core.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.exceptions.CouponSystemException;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;
import app.core.utils.CouponPayload;
import app.core.utils.ImageUtil;

import static app.core.utils.Constants.EMAIL_PATTERN;
import static app.core.utils.Constants.PASSWORD_MIN_LENGTH;

public abstract class ClientService {
	
	// Attributes
	@Autowired
	protected CompanyRepository companyRepository;
	@Autowired
	protected CustomerRepository customerRepository;
	@Autowired
	protected CouponRepository couponRepository;
	@Autowired
	protected ImageUtil imageUtil;
	
	
	// Methods
	public abstract boolean login(String email, String password);
	
	protected void validateCompanyData(Company company) throws CouponSystemException {
		
		// Company is null
		if (company == null) {
			throw new CouponSystemException("Company is null");
		}
		
		// Company's Name is null
		if (company.getName() == null) {
			throw new CouponSystemException("Company's Name is null");
		}
		
		// Company's Email is null
		if (company.getEmail() == null) {
			throw new CouponSystemException("Company's Email is null");
		}
		
		// Company's Password is null
		if (company.getPassword() == null) {
			throw new CouponSystemException("Company's Password is null");
		}
		
		// Email must be valid [ ___@__.__ ]
		if (!company.getEmail().matches(EMAIL_PATTERN)) {
			throw new CouponSystemException("Please enter the valid Email format [ ___@__.com ]");
		}
		
		// Password must be at least 5 characters long
		if (company.getPassword().length() < PASSWORD_MIN_LENGTH) {
			throw new CouponSystemException("Password must be at least " + PASSWORD_MIN_LENGTH + " characters long");
		}
		
	}
	
	protected void validateCustomerData(Customer customer) throws CouponSystemException {
		
		// Customer is null
		if (customer == null) {
			throw new CouponSystemException("Customer is null");
		}
		
		// Customer's Email is null
		if (customer.getEmail() == null) {
			throw new CouponSystemException("Customer's Email is null");
		}
		
		// Customer's Password is null
		if (customer.getPassword() == null) {
			throw new CouponSystemException("Customer's Password is null");
		}
		
		// Email must be valid [ ___@__.__ ]
		if (!customer.getEmail().matches(EMAIL_PATTERN)) {
			throw new CouponSystemException("Please enter the valid Email format [ ___@__.com ]");
		}
		
		// Password must be at least 5 characters long
		if (customer.getPassword().length() < PASSWORD_MIN_LENGTH) {
			throw new CouponSystemException("Password must be at least " + PASSWORD_MIN_LENGTH + " characters long");
		}
		
	}
	
	protected void validateCouponData(Coupon coupon) throws CouponSystemException {
		
		// Coupon is null
		if (coupon == null) {
			throw new CouponSystemException("Coupon is null");
		}
		
		// Coupon's End Date is null
		if (coupon.getEndDate() == null) {
			throw new CouponSystemException("Coupon's End Date is null");
		}
		
		// Coupon's Title is null
		if (coupon.getTitle() == null) {
			throw new CouponSystemException("Coupon's Title is null");
		}
		
		// Coupon's Amount is a negative value (can be 0)
		if (coupon.getAmount() < 0) {
			throw new CouponSystemException("Coupon's Amount cannot equal to " + coupon.getAmount());
		}
		
		// Coupon's Price is a negative value (can be 0)
		if (coupon.getPrice() < 0) {
			throw new CouponSystemException("Coupon's Price cannot equal to " + coupon.getPrice());
		}
		
		// File (if there's one) name must not contain illegal characters (..)
		if (coupon.getImage() != null && coupon.getImage().contains("..")) {
			throw new CouponSystemException("Files names must not contain illegal characters '..'");
		}
		
		// Coupon's End Date is a date that is already expired
		if (coupon.getEndDate().isBefore(LocalDate.now())) {
			throw new CouponSystemException("End Date cannot be an already expired date (" + coupon.getEndDate() + ")");
		}
		
	}
	
	protected void validateCouponOwnership(Coupon coupon, int currCompanyID) throws CouponSystemException {
		
		// Coupon's company is null
		if (coupon.getCompany() == null) {
			throw new CouponSystemException("Coupon's Company is null");
		}
		
		// Checks if the current company owns this coupon and permitted to operate on it
		int couponsCompanyID = coupon.getCompany().getId();
		if (currCompanyID != couponsCompanyID) {
			throw new CouponSystemException("Cannot Operate on this Coupon! You do not own it!");
		}
		
	}
	
	protected Coupon unpackCouponPayload(CouponPayload payload) {
		Coupon coupon = new Coupon();
		
		coupon.setId(payload.getId());
		coupon.setCompany(companyRepository.getById(payload.getCompany()));
		coupon.setTitle(payload.getTitle());
		coupon.setCategory(payload.getCategory());
		coupon.setDescription(payload.getDescription());
		coupon.setStartDate(payload.getStartDate());
		coupon.setEndDate(payload.getEndDate());
		coupon.setPrice(payload.getPrice());
		coupon.setAmount(payload.getAmount());
		
		// Payload's Image and Customers are handled separately
		
		return coupon;
	}
	
	protected CouponPayload packCouponPayload(Coupon coupon, MultipartFile image) {
		CouponPayload couponPayload = new CouponPayload();
		
		couponPayload.setId(coupon.getId());
		couponPayload.setCompany(coupon.getCompany().getId());
		couponPayload.setTitle(coupon.getTitle());
		couponPayload.setCategory(coupon.getCategory());
		couponPayload.setDescription(coupon.getDescription());
		if (coupon.getStartDate() != null) couponPayload.setStartDate(coupon.getStartDate().toString());
		if (coupon.getEndDate() != null) couponPayload.setEndDate(coupon.getEndDate().toString());
		couponPayload.setPrice(coupon.getPrice());
		couponPayload.setAmount(coupon.getAmount());
		
		// Coupon's Image is retrieved separately
		
		return couponPayload;
	}
	
}
