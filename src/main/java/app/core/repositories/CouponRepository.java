package app.core.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.core.entities.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
	
	// For finding duplicate Company's Coupon Title
	Optional<Coupon> findByTitleAndCompanyId(String title, int companyId);
	
	// For retrieving expired Coupons
	List<Coupon> findByEndDateIsBefore(LocalDate date);
	
}
