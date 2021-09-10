package app.core.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.core.exceptions.LoginManagerException;
import app.core.services.AdminService;
import app.core.services.ClientService;
import app.core.services.CompanyService;
import app.core.services.CustomerService;

@Component
public class LoginManager {

	// Attribute
	@Autowired
	private ApplicationContext ctx;
	
	
	// Method
	public ClientService login(Credentials cred) throws LoginManagerException {

		if (cred.email == null) throw new LoginManagerException("Cannot Login, Email is null");
		if (cred.password == null) throw new LoginManagerException("Cannot Login, Password is null");
		if (cred.clientType == null) throw new LoginManagerException("Cannot Login, ClientType is null");
		
		ClientService service;
		
		switch (cred.clientType) {
		
			case Administrator:
				service = ctx.getBean(AdminService.class);
				break;
			case Company:
				service = ctx.getBean(CompanyService.class);
				break;
			case Customer:
				service = ctx.getBean(CustomerService.class);
				break;
				
			default:
				throw new LoginManagerException("Couldn't find the right Client Type to Login");
		}

		if (service.login(cred.email, cred.password)) {
			return service;
		}

		return null;
	}

}
