package main.java.com.expense.tracker.cli.models;

import java.time.LocalDate;

public class Expense {
	
	// Attributes
	
	private int id;
	private LocalDate date;
	private String description;
	private double amount;
	
	public Expense(
			int id, 
			LocalDate date, 
			String description, 
			double amount) {
		this.id = id;
		this.date = date;
		this.description = description;
		this.amount = amount;
	}
	
	// getters
	
	public int getId() {
		return this.id;
	}
	
	public LocalDate getDate() {
		return this.date;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public double getAmount() {
		return this.amount;
	}
	
	// setters
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
