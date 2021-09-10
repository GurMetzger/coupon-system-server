package app.core.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.core.entities.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
	
	// For Login purposes
	Optional<Company> findByEmailAndPassword(String email, String password);
	
	// For finding duplicate Company Emails
	Optional<Company> findByEmail(String email);
	
	// For finding duplicate Company Names
	Optional<Company> findByName(String name);
	
}
