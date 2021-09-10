package app.core.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import app.core.entities.Category;
import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.exceptions.CouponAlreadyOwnedException;
import app.core.exceptions.CouponNotFoundException;
import app.core.exceptions.CustomerNotFoundException;
import app.core.exceptions.CustomerServiceException;
import app.core.exceptions.InvalidArgumentException;

@Service
@Transactional(rollbackOn = CustomerServiceException.class) // Failed transactions will result in a rollback
public class CustomerService extends ClientService {

	// Methods
	@Override
	public boolean login(String email, String password) { // LoginManager is already checking for null
		return customerRepository.findByEmailAndPassword(email, password).isPresent();
	}

	public Coupon purchaseCoupon(Coupon coupon, int customerID) throws CustomerNotFoundException, CouponNotFoundException, 
	CouponAlreadyOwnedException, InvalidArgumentException {
		
		if (coupon == null) throw new InvalidArgumentException("Cannot purchase, Coupon is null");
		
		Optional<Customer> optCustomer = customerRepository.findById(customerID);
		if (optCustomer.isPresent()) {
			
			Optional<Coupon> optCoupon = couponRepository.findById(coupon.getId());
			if (optCoupon.isPresent()) {
				
				Customer customerDb = optCustomer.get();
				Coupon couponDb = optCoupon.get();
				
				// Checks if there are any coupons left to purchase
				if (couponDb.getAmount() < 1) {
					throw new InvalidArgumentException("Coupon ran out of stock!");
				}

				// Checks if the coupon has expired
				if (couponDb.getEndDate().isBefore(LocalDate.now())) {
					throw new InvalidArgumentException("Coupon has expired!");
				}
				
				// Checks if customer has already purchased this coupon
				if (customerDb.getCoupons().contains(couponDb)) {
					throw new CouponAlreadyOwnedException("You already own this Coupon!");
				}
				
				{
					// Purchases the coupon
					customerDb.addCoupon(couponDb);
					
					// ... and reduce its amount by 1
					couponDb.setAmount(couponDb.getAmount() - 1);
					
					return couponRepository.save(couponDb);
				}
				
			} else {
				throw new CouponNotFoundException("Coupon with ID '" + coupon.getId() + "' doesn't exist");
			}

		} else {
			throw new CustomerNotFoundException("Couldn't find Customer with ID '" + customerID + "' to purchase coupon");
		}
	}

	public List<Coupon> getCustomerCoupons(int customerID) throws CustomerNotFoundException {

		Optional<Customer> opt = customerRepository.findById(customerID);
		if (opt.isPresent()) {
			
			return opt.get().getCoupons().stream()
					.collect(Collectors.toList());
			
		} else {
			throw new CustomerNotFoundException("Couldn't find Customer with ID '" + customerID + "' to get coupons");
		}
	}

	public List<Coupon> getCustomerCoupons(Category category, int customerID) throws InvalidArgumentException, 
	CustomerNotFoundException {

		if (category == null) {
			throw new InvalidArgumentException("Invalid category '" + category + "' value");
		}
		
		return getCustomerCoupons(customerID).stream()
				.filter(coupon -> coupon.getCategory() == category)
				.collect(Collectors.toList());
	}

	public List<Coupon> getCustomerCoupons(double maxPrice, int customerID) throws InvalidArgumentException, 
	CustomerNotFoundException {

		if (maxPrice < 0) {
			throw new InvalidArgumentException("Invalid price '" + maxPrice + "' value");
		}

		return getCustomerCoupons(customerID).stream()
				.filter(coupon -> coupon.getPrice() <= maxPrice)
				.collect(Collectors.toList());	
	}

	public Customer getCustomerDetails(int customerID) throws CustomerNotFoundException {

		Optional<Customer> opt = customerRepository.findById(customerID);
		if (opt.isPresent()) {
			
			Hibernate.initialize(opt.get().getCoupons());
			return opt.get();
			
		} else {
			throw new CustomerNotFoundException("Customer with ID '" + customerID + "' doesn't exist");
		}
	}
	
}
