package app.core.services;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.core.entities.Coupon;
import app.core.exceptions.CouponSystemException;
import app.core.repositories.CouponRepository;

@Service
@Transactional(rollbackOn = CouponSystemException.class) // Failed transactions will result in a rollback
public class ExpirationJobService {

	// Attribute
	@Autowired
	private CouponRepository couponRepository;

	
	// Method	
	public void deleteAllExpiredCoupons() {
		
		// Retrieves all Coupons that have expired
		List<Coupon> expiredCoupons = couponRepository.findByEndDateIsBefore(LocalDate.now());
		
		// Deletes the expired Coupons
		expiredCoupons.stream().forEach(coupon -> couponRepository.deleteById(coupon.getId()));
				
	}
	
}
