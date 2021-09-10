package app.core.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import app.core.services.ExpirationJobService;
import static app.core.utils.Constants.JOB_DEADTIME_IN_MILLIS;

@Component
public class CouponExpirationJob {

	// Attribute
	@Autowired
	private ExpirationJobService jobService;
	
	
	// Method
	@Scheduled(fixedRate = JOB_DEADTIME_IN_MILLIS)
	public void doJob() {		
		jobService.deleteAllExpiredCoupons();
	}
	
}
