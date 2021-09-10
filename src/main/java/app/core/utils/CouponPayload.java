package app.core.utils;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import app.core.entities.Category;

public class CouponPayload {
	
	// The Coupon Payload sent from the Front-End in order to Add/Update/Get Coupon(s)

	// Attributes
	private int id;
	private int company;
	private String title;
	private Category category;
	private String description;
	private LocalDate startDate;
	private LocalDate endDate;
	private double price;
	private int amount;
	private MultipartFile image;
	
	private boolean keepImage; // Field is used strictly in Updating a Coupon

	
	// Getters & Setters
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getCompany() {
		return company;
	}
	
	public void setCompany(int company) {
		this.company = company;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	public void setStartDate(String startDate) {
		this.startDate = LocalDate.parse(startDate);
	}

	public LocalDate getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = LocalDate.parse(endDate);
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public MultipartFile getImage() {
		return image;
	}
	
	public void setImage(MultipartFile image) {
		this.image = image;
	}
	
	public boolean isKeepImage() {
		return keepImage;
	}

	public void setKeepImage(boolean keepImage) {
		this.keepImage = keepImage;
	}

	
	// Overriden Method
	@Override
	public String toString() {
		return "CouponPayload [id=" + id + ", company=" + company + ", title=" + title + ", category=" + category
				+ ", description=" + description + ", startDate=" + startDate + ", endDate=" + endDate + ", price="
				+ price + ", amount=" + amount + ", image=" + image + ", keepImage=" + keepImage + "]";
	}
	
}
