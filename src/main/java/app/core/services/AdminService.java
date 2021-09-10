package app.core.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.exceptions.AdminServiceException;
import app.core.exceptions.CompanyEmailAlreadyExistsException;
import app.core.exceptions.CompanyNameAlreadyExistsException;
import app.core.exceptions.CompanyNameUpdateException;
import app.core.exceptions.CompanyNotFoundException;
import app.core.exceptions.CouponNotFoundException;
import app.core.exceptions.CouponSystemException;
import app.core.exceptions.CustomerEmailAlreadyExistsException;
import app.core.exceptions.CustomerNotFoundException;
import app.core.exceptions.DatabaseMalfunctionAdminException;
import app.core.exceptions.InvalidCompanyDataException;
import app.core.exceptions.InvalidCustomerDataException;

@Service
@Transactional(rollbackOn = AdminServiceException.class) // Failed transactions will result in a rollback
public class AdminService extends ClientService {

	// Methods
	@Override
	public boolean login(String email, String password) { // LoginManager is already checking for null		
		return email.equals("admin@admin.com") && password.equals("admin");
	}
	
	public List<Coupon> getAllCoupons() {
		return couponRepository.findAll();
	}
	
	public Coupon getOneCoupon(int couponID) throws CouponNotFoundException {
		
		Optional<Coupon> opt = couponRepository.findById(couponID);
		if (opt.isPresent()) {
			return opt.get();
		} else {
			throw new CouponNotFoundException("Coupon with ID '" + couponID + "' doesn't exist");
		}
	}

	public Company addCompany(Company company) throws InvalidCompanyDataException, CompanyNameAlreadyExistsException, 
	CompanyEmailAlreadyExistsException, DatabaseMalfunctionAdminException {
		
		// Data validation
		try {
			validateCompanyData(company);
		} catch (CouponSystemException e) {
			throw new InvalidCompanyDataException("Cannot add Company, " + e.getMessage());
		}
		
		// A company with an already existing Name cannot be added
		if (companyRepository.findByName(company.getName()).isPresent()) {
			throw new CompanyNameAlreadyExistsException("Company with Name '" + company.getName() + "' already exists");
		}
		
		// A company with an already existing Email cannot be added
		if (companyRepository.findByEmail(company.getEmail()).isPresent()) {
			throw new CompanyEmailAlreadyExistsException("Company with Email '" + company.getEmail() + "' already exists");
		}

		// If the ID value points to a non-existent company - will try to add it.
		// If the ID value points to an already existing company - will try to update it.
		// Because of this, ID here must be set to 0 (default value for adding).
		company.setId(0);
		
		try {			
			return companyRepository.save(company);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseMalfunctionAdminException("Could not add company: " + e.getMostSpecificCause());
		}
		
	}

	public Company updateCompany(Company company) throws InvalidCompanyDataException, CompanyEmailAlreadyExistsException,
	CompanyNameUpdateException, CompanyNotFoundException {

		// Checking for changes to company ID is not needed here. Spring doesn't allow
		// changes to ID.
		//
		// ID    - not updatable (by Spring)
		// Name  - not updatable
		// Email - updatable but not to an existing email
		
		// Data validation
		try {
			validateCompanyData(company);
		} catch (CouponSystemException e) {
			throw new InvalidCompanyDataException("Cannot update Company, " + e.getMessage());
		}
		
		// A company cannot be updated to have an already existing Email (Will ignore if Email has not changed)
		Optional<Company> optEmail = companyRepository.findByEmail(company.getEmail());
		if (optEmail.isPresent() && optEmail.get().getId() != company.getId()) {
			throw new CompanyEmailAlreadyExistsException("Company with Email '" + company.getEmail() + "' already exists");
		}
		
		Optional<Company> opt = companyRepository.findById(company.getId());
		if (opt.isPresent()) {
			
			// A company's Name cannot be updated
			if (!opt.get().getName().equals(company.getName())) {
				throw new CompanyNameUpdateException("Updating a Company's name is forbidden");
			}
			
			// Persists the Company's Coupons
			company.setCoupons(opt.get().getCoupons());
			
			// If finds a company with the ID value - will update it.
			return companyRepository.save(company);
			
		} else {
			throw new CompanyNotFoundException("Company with ID '" + company.getId() + "' doesn't exist");
		}

	}

	public int deleteCompany(int companyID) throws CompanyNotFoundException {
		try {
			
			// Will automatically delete the company's coupons and the coupons' purchases
			companyRepository.deleteById(companyID);
			return companyID;
			
		} catch (EmptyResultDataAccessException e) {
			throw new CompanyNotFoundException("Company with ID '" + companyID + "' doesn't exist");
		}
	}

	public List<Company> getAllCompanies() {
		List<Company> companies = companyRepository.findAll();
		
		return companies.stream()
				.peek(company -> Hibernate.initialize(company.getCoupons()))
				.collect(Collectors.toList());
	}

	public Company getOneCompany(int companyID) throws CompanyNotFoundException {

		Optional<Company> opt = companyRepository.findById(companyID);
		if (opt.isPresent()) {
			
			Hibernate.initialize(opt.get().getCoupons());
			return opt.get();
			
		} else {
			throw new CompanyNotFoundException("Company with ID '" + companyID + "' doesn't exist");
		}
	}

	public Customer addCustomer(Customer customer) throws InvalidCustomerDataException, CustomerEmailAlreadyExistsException,
	DatabaseMalfunctionAdminException {
		
		// Data validation
		try {
			validateCustomerData(customer);
		} catch (CouponSystemException e) {
			throw new InvalidCustomerDataException("Cannot add, " + e.getMessage());
		}
		
		// A customer with an already existing Email cannot be added
		if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
			throw new CustomerEmailAlreadyExistsException("Customer with Email '" + customer.getEmail() +"' already exists");
		}

		// If the ID value points to a non-existent customer - will try to add it.
		// If the ID value points to an already existing customer - will try to update it.
		// Because of this, ID here must be set to 0 (default value for adding).
		customer.setId(0);
		
		try {			
			return customerRepository.save(customer);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseMalfunctionAdminException("Could not add customer: " + e.getMostSpecificCause());
		}

	}

	public Customer updateCustomer(Customer customer) throws InvalidCustomerDataException, CustomerNotFoundException, 
	CustomerEmailAlreadyExistsException {

		// Checking for changes to customer ID is not needed here. Spring doesn't allow
		// changes to ID.
		//
		// ID    - not updatable (by Spring)
		// Email - updatable but not to existing email
		
		// Data validation
		try {
			validateCustomerData(customer);
		} catch (CouponSystemException e) {
			throw new InvalidCustomerDataException("Cannot update Customer, " + e.getMessage());
		}
		
		// A customer cannot be updated to have an already existing Email (Will ignore if Email has not changed)
		Optional<Customer> optEmail = customerRepository.findByEmail(customer.getEmail());
		if (optEmail.isPresent() && optEmail.get().getId() != customer.getId()) {
			throw new CustomerEmailAlreadyExistsException("Customer with Email '" + customer.getEmail() +"' already exists");
		}
		
		Optional<Customer> opt = customerRepository.findById(customer.getId());
		if (opt.isPresent()) {
			
			// Persists the Customer's purchases
			customer.setCoupons(opt.get().getCoupons());
			
			// If finds a customer with the ID value - will update it.
			return customerRepository.save(customer);
			
		} else {
			throw new CustomerNotFoundException("Customer with ID '" + customer.getId() + "' doesn't exist");
		}

	}

	public int deleteCustomer(int customerID) throws CustomerNotFoundException {
		
		try {
			
			// Will automatically delete the customer's coupon purchases
			customerRepository.deleteById(customerID);
			return customerID;
			
		} catch (EmptyResultDataAccessException e) {
			throw new CustomerNotFoundException("Customer with ID '" + customerID + "' doesn't exist");
		}

	}

	public List<Customer> getAllCustomers() {
		List<Customer> customers = customerRepository.findAll();
		
		return customers.stream()
				.peek(customer ->  Hibernate.initialize(customer.getCoupons()))
				.collect(Collectors.toList());
	}

	public Customer getOneCustomer(int customerID) throws CustomerNotFoundException {

		Optional<Customer> opt = customerRepository.findById(customerID);
		if (opt.isPresent()) {
			
			Hibernate.initialize(opt.get().getCoupons());
			return opt.get();
			
		} else {
			throw new CustomerNotFoundException("Customer with ID '" + customerID + "' doesn't exist");
		}
	}

}
