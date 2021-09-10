package app.core.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exceptions.CompanyEmailAlreadyExistsException;
import app.core.exceptions.CompanyNameAlreadyExistsException;
import app.core.exceptions.CompanyNotFoundException;
import app.core.exceptions.CouponSystemException;
import app.core.exceptions.CredentialsException;
import app.core.exceptions.CustomerEmailAlreadyExistsException;
import app.core.exceptions.CustomerNotFoundException;
import app.core.exceptions.DatabaseMalfunctionAdminException;
import app.core.exceptions.InvalidCompanyDataException;
import app.core.exceptions.InvalidCustomerDataException;
import app.core.exceptions.LoginFailedException;
import app.core.exceptions.LoginManagerException;
import app.core.login.ClientDetails;
import app.core.login.ClientType;
import app.core.login.Credentials;
import app.core.login.LoginManager;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CustomerRepository;
import app.core.utils.JwtUtil;

import static app.core.utils.Constants.EMAIL_PATTERN;
import static app.core.utils.Constants.LOGIN_FAIL_MESSAGE;
import static app.core.utils.Constants.PASSWORD_MIN_LENGTH;

@Service
@Transactional(rollbackOn = CouponSystemException.class) // Failed transactions will result in a rollback
public class AuthService {
	
	// Attributes
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private AdminService adminService;
	@Autowired
	private LoginManager loginManager;
	@Autowired
	private JwtUtil jwtUtil;
	

	// Methods
	public ClientDetails loginAdmin(Credentials credentials) throws LoginFailedException, CredentialsException {		
		try {
			
			checkCredentials(credentials);
			
			if (loginSucessful(credentials)) {
				
				int id = 0;
				String email = "admin@admin.com";
				String name = "Admin";
				String token = jwtUtil.generateToken(id, name, email, ClientType.Administrator);
				
				return clientBuilder(id, name, email, ClientType.Administrator, token);
			}
			
			throw new LoginFailedException(LOGIN_FAIL_MESSAGE);
			
		} catch (CredentialsException | LoginManagerException e) {
			throw new CredentialsException(e.getMessage());
		}
	}
	
	public ClientDetails loginCompany(Credentials credentials) throws LoginFailedException, CredentialsException, 
	CompanyNotFoundException {		
		try {
			
			checkCredentials(credentials);
			
			if (loginSucessful(credentials)) {
				
				int id = getCompanyID(credentials.email);
				String name = getCompanyName(credentials.email);
				String email = credentials.email;
				String token = jwtUtil.generateToken(id, name,  email, ClientType.Company);
				
				return clientBuilder(id, name, email, ClientType.Company, token);
			}
			
			throw new LoginFailedException(LOGIN_FAIL_MESSAGE);
			
		} catch (CredentialsException | LoginManagerException e) {
			throw new CredentialsException(e.getMessage());
		} catch (CompanyNotFoundException e) {
			throw new CompanyNotFoundException(e.getMessage());
		}
	}

	public ClientDetails loginCustomer(Credentials credentials) throws LoginFailedException, CredentialsException, 
	CustomerNotFoundException {
		try {
			
			checkCredentials(credentials);
			
			if (loginSucessful(credentials)) {
				
				int id = getCustomerID(credentials.email);
				String name = getCustomerName(credentials.email);
				String email = credentials.email;
				String token = jwtUtil.generateToken(id, name, email, ClientType.Customer);
				
				return clientBuilder(id, name, email, ClientType.Customer, token);
			}
			
			throw new LoginFailedException(LOGIN_FAIL_MESSAGE);
			
		} catch (CredentialsException | LoginManagerException e) {
			throw new CredentialsException(e.getMessage());
		} catch (CustomerNotFoundException e) {
			throw new CustomerNotFoundException(e.getMessage());
		}
	}
	
	public ClientDetails registerCompany(Company companyRegistering) throws InvalidCompanyDataException, 
	CompanyNameAlreadyExistsException, CompanyEmailAlreadyExistsException, DatabaseMalfunctionAdminException {
		
		Company company = adminService.addCompany(companyRegistering);
		
		return clientBuilder(company.getId(), 
				company.getName(), 
				company.getEmail(), 
				ClientType.Company, 
				jwtUtil.generateToken(company.getId(), company.getName(), company.getEmail(), ClientType.Company));
	}
	
	public ClientDetails registerCustomer(Customer customerRegistering) throws InvalidCustomerDataException, 
	CustomerEmailAlreadyExistsException, DatabaseMalfunctionAdminException {
		
		Customer customer = adminService.addCustomer(customerRegistering);
		
		String customerName = packCustomerName(customer.getFirstName(), customer.getLastName());
		
		return clientBuilder(customer.getId(), 
				customerName, 
				customer.getEmail(), 
				ClientType.Customer, 
				jwtUtil.generateToken(customer.getId(), customerName, customer.getEmail(), ClientType.Customer));
	}
	
	private void checkCredentials(Credentials credentials) throws CredentialsException {
			
		if (credentials.email == null) throw new CredentialsException("Email must be included in the credentials");
		if (credentials.password == null) throw new CredentialsException("Password must be included in the credentials");
		if (credentials.clientType == null) throw new CredentialsException("ClientType must be included in the credentials");
			
		// Email must be valid [ ___@__.com ]	
		if (!credentials.email.matches(EMAIL_PATTERN)) {
			throw new CredentialsException("Please enter the valid Email format [ ___@__.com ]");
		}
			
		// Password must be at least 5 characters long
		if (credentials.password.length() < PASSWORD_MIN_LENGTH) {
			throw new CredentialsException("Password must be at least " + PASSWORD_MIN_LENGTH + " characters long");
		}
	}
	
	private boolean loginSucessful(Credentials cred) throws LoginManagerException {
		ClientService service = loginManager.login(cred);
		return service != null;
	}
	
	private int getCompanyID(String email) throws CompanyNotFoundException {
		Optional<Company> opt = companyRepository.findByEmail(email);
		if (opt.isPresent()) {
			return opt.get().getId();
		} else {
			throw new CompanyNotFoundException("Cannot get, Company with Email '" + email + "' doesn't exist");
		}
	}
	
	private String getCompanyName(String email) throws CompanyNotFoundException {
		Optional<Company> opt = companyRepository.findByEmail(email);
		if (opt.isPresent()) {
			return opt.get().getName();
		} else {
			throw new CompanyNotFoundException("Cannot get, Company with Email '" + email + "' doesn't exist");
		}
	}
	
	private int getCustomerID(String email) throws CustomerNotFoundException {
		Optional<Customer> opt = customerRepository.findByEmail(email);
		if (opt.isPresent()) {
			return opt.get().getId();
		} else {
			throw new CustomerNotFoundException("Cannot get, Customer with Email '" + email + "' doesn't exist");
		}
	}
	
	private String getCustomerName(String email) throws CustomerNotFoundException {
		Optional<Customer> opt = customerRepository.findByEmail(email);
		if (opt.isPresent()) {
			
			String firstName = opt.get().getFirstName();
			String lastName = opt.get().getLastName();
			
			return packCustomerName(firstName, lastName);
			
		} else {
			throw new CustomerNotFoundException("Cannot get, Customer with Email '" + email + "' doesn't exist");
		}
	}
	
	private ClientDetails clientBuilder(int id, String name, String email, ClientType clientType, String token) {
		return new ClientDetails.Builder()
				.setId(id)
				.setName(name)
				.setEmail(email)
				.setClientType(clientType)
				.setToken(token)
				.build();
	}
	
	private String packCustomerName(String firstName, String lastName) {
		String name = "";
		
		if (firstName != null && !firstName.isBlank()) name += firstName;
		if (lastName != null && !lastName.isBlank()) name += " " + lastName;
		
		return !name.isEmpty() ? name : "Unnamed Customer";
	}

}
