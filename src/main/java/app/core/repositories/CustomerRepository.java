package app.core.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.core.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	// For Login purposes
	Optional<Customer> findByEmailAndPassword(String email, String password);
	
	// For finding duplicate Customer Emails
	Optional<Customer> findByEmail(String email);
	
}
