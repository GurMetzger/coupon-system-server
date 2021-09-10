package app.core.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import app.core.entities.Category;
import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.exceptions.CompanyNotFoundException;
import app.core.exceptions.CompanyServiceException;
import app.core.exceptions.CouponNotFoundException;
import app.core.exceptions.CouponSystemException;
import app.core.exceptions.CouponTitleAlreadyExistsException;
import app.core.exceptions.DatabaseMalfunctionCompanyException;
import app.core.exceptions.InvalidArgumentException;
import app.core.exceptions.InvalidCouponDataException;
import app.core.utils.CouponPayload;

@Service
@Transactional(rollbackOn = CompanyServiceException.class) // Failed transactions will result in a rollback
public class CompanyService extends ClientService {
	
	// Methods
	@Override
	public boolean login(String email, String password) { // LoginManager is already checking for null
		return companyRepository.findByEmailAndPassword(email, password).isPresent();
	}

	public Coupon addCoupon(CouponPayload couponPayload, int companyID) throws InvalidCouponDataException, 
	CompanyNotFoundException, DatabaseMalfunctionCompanyException, CouponTitleAlreadyExistsException {
		
		Coupon coupon = unpackCouponPayload(couponPayload);
		MultipartFile image = couponPayload.getImage();
		
		// Data validation
		try {
			validateCouponData(coupon);
		} catch (CouponSystemException e) {
			throw new InvalidCouponDataException("Cannot add, " + e.getMessage());
		}

		Optional<Company> opt = companyRepository.findById(companyID);
		if (opt.isPresent()) {
			
			// Each of a single Company's Coupons must have a unique Title
			if (couponRepository.findByTitleAndCompanyId(coupon.getTitle(), companyID).isPresent()) {
				throw new CouponTitleAlreadyExistsException("You already created Coupon with Title '"+coupon.getTitle()+"'");
			}
			
			// If the ID value points to a non-existent coupon - will try to add it.
			// If the ID value points to an already existing coupon - will try to update it.
			// Because of this, ID here must be set to 0 (default value for adding).
			coupon.setId(0);
			
			// Assign the Coupon its Company
			coupon.setCompany(opt.get());

			// Saves the Image and stores its Name in the database
			coupon.setImage(imageUtil.storeImage(image));
			
			try {
				return couponRepository.save(coupon);
			} catch (DataIntegrityViolationException | JpaSystemException e) {
				throw new DatabaseMalfunctionCompanyException("Could not add coupon: " + e.getMostSpecificCause());
			}
			
		} else {
			throw new CompanyNotFoundException("Couldn't find Company with ID '" + companyID + "' to add the coupon");
		}
	}

	public Coupon updateCoupon(CouponPayload couponPayload, int companyID) throws InvalidCouponDataException,
	CouponTitleAlreadyExistsException, CouponNotFoundException {
		
		Coupon coupon = unpackCouponPayload(couponPayload);
		
		// Checking for changes to ID and/or coupon companyID is not needed here. Spring doesn't
		// allow changes to ID value, and coupon companyID was declared not updatable in the entity layer.
		//
		// ID        - not updatable (by Spring)
		// CompanyID - not updatable
		// Title     - updatable but not to an existing title of a company's coupons
		
		try {
			
			// Validating the Coupon's data
			validateCouponData(coupon);
			
			// Validating that the current Company owns and can update the Coupon
			validateCouponOwnership(coupon, companyID);
			
		} catch (CouponSystemException e) {
			throw new InvalidCouponDataException("Cannot update, " + e.getMessage());
		}
		
		Optional<Coupon> opt = couponRepository.findById(coupon.getId());
		if (opt.isPresent()) {
			
			// Each of a single Company's Coupons must have a unique Title (Ignores if title has not changed)
			Optional<Coupon> optCompanyTitle = couponRepository.findByTitleAndCompanyId(coupon.getTitle(), companyID);
			if (optCompanyTitle.isPresent() && optCompanyTitle.get().getId() != coupon.getId()) {
				throw new CouponTitleAlreadyExistsException("You already created Coupon with Title '"+coupon.getTitle()+"'");
			}
			
			// Keep same Image
			if (couponPayload.isKeepImage()) {
				coupon.setImage(opt.get().getImage());
			}
			// Assign a new Image
			if (couponPayload.getImage() != null) {
				coupon.setImage(imageUtil.storeImage(couponPayload.getImage()));
			}
			// If none of the above, Image is removed
			
			// Persists the Customer's purchases
			coupon.setCustomers(opt.get().getCustomers());
			
			// If finds a coupon with the ID value and matching companies - will update it.
			return couponRepository.save(coupon);
			
		} else {
			throw new CouponNotFoundException("Coupon with ID '" + coupon.getId() + "' doesn't exist");
		}
	}

	public int deleteCoupon(int couponID, int companyID) throws InvalidCouponDataException, CouponNotFoundException {

		Optional<Coupon> opt = couponRepository.findById(couponID);
		if (opt.isPresent()) {

			try {
				// Validating that the current Company owns and can delete the Coupon
				validateCouponOwnership(opt.get(), companyID);
			} catch (CouponSystemException e) {
				throw new InvalidCouponDataException("Cannot delete, " + e.getMessage());
			}

			// Will automatically delete the coupon's purchases
			couponRepository.deleteById(couponID);
			return couponID;
			
		} else {
			throw new CouponNotFoundException("Coupon with ID '" + couponID + "' doesn't exist");
		}
	}

	public List<Coupon> getCompanyCoupons(int companyID) throws CompanyNotFoundException {

		Optional<Company> opt = companyRepository.findById(companyID);
		if (opt.isPresent()) {
			
			Hibernate.initialize(opt.get().getCoupons());
			return opt.get().getCoupons();
			
		} else {
			throw new CompanyNotFoundException("Couldn't find Company with ID '" + companyID + "' to get coupons");
		}
	}

	public List<Coupon> getCompanyCoupons(Category category, int companyID) throws InvalidArgumentException, 
	CompanyNotFoundException {

		if (category == null) {
			throw new InvalidArgumentException("Invalid category '" + category + "' value");
		}

		return getCompanyCoupons(companyID).stream()
				.filter(coupon -> coupon.getCategory() == category)
				.collect(Collectors.toList());		
	}

	public List<Coupon> getCompanyCoupons(double maxPrice, int companyID) throws InvalidArgumentException, 
	CompanyNotFoundException {

		if (maxPrice < 0) {
			throw new InvalidArgumentException("Invalid price '" + maxPrice + "' value");
		}
		
		return getCompanyCoupons(companyID).stream()
				.filter(coupon -> coupon.getPrice() <= maxPrice)
				.collect(Collectors.toList());
	}

	public Company getCompanyDetails(int companyID) throws CompanyNotFoundException {

		Optional<Company> opt = companyRepository.findById(companyID);
		if (opt.isPresent()) {
			
			Hibernate.initialize(opt.get().getCoupons());
			return opt.get();
			
		} else {
			throw new CompanyNotFoundException("Company with ID '" + companyID + "' doesn't exist");
		}
	}

}
