package app.core.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Check;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "company_id", "title" }))
@Check(constraints = "amount >= 0 and price >= 0")
@JsonIgnoreProperties("customers") // Won't display Customers in Coupon JSON
public class Coupon {
	
	// Attributes
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Enumerated(EnumType.STRING)
	private Category category;
	@Column(nullable = false)
	private String title;
	private String description;
	private LocalDate startDate;
	@Column(nullable = false)
	private LocalDate endDate;
	private int amount;
	private double price;
	private String image;
	@ManyToOne
	@JoinColumn(nullable = false, updatable = false)
	@JsonIdentityReference(alwaysAsId = true) // Will ALWAYS display Company's ID in Coupon JSON
	private Company company;
	
	@ManyToMany
	@JoinTable(
			name = "customers_vs_coupons", 
			joinColumns = @JoinColumn(name = "coupon_id"), 
			inverseJoinColumns = @JoinColumn(name = "customer_id"))
	private Set<Customer> customers;
	
	
	// CTORs
	public Coupon(Category category, String title, String description, LocalDate startDate, LocalDate endDate, 
			int amount, double price, String image) {
		super();
		this.category = category;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.price = price;
		this.image = image;
	}
	
	public Coupon() {
		
	}
	
	
	// Methods
	public void addCustomer(Customer customer) {
		if (customer == null) {
			return;
		}
		if (this.customers == null) {
			this.customers = new HashSet<>();
		}
		
		this.customers.add(customer);
	}
	
	public void removeCustomer(Customer customer) {
		if (customer == null || this.customers == null) {
			return;
		}
		
		this.customers.remove(customer);
	}
	
	@Override
	public String toString() {
		return "Coupon [id=" + id + ", companyID=" + (company != null ? company.getId() : "none") + ", category=" + 
				category + ", title=" + title + ", description=" + description + ", startDate=" + startDate + 
				", endDate=" + endDate + ", amount=" + amount + ", price=" + price + ", image=" + image + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Coupon)) {
			return false;
		}
		Coupon other = (Coupon) obj;
		return id == other.id;
	}
	

	// Getters & Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Set<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}
	
}
