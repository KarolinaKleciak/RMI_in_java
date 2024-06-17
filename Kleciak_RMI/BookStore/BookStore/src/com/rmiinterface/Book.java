package com.rmiinterface;

import java.io.Serializable;

public class Book implements Serializable {
	private static final long serialVersionUID = 11L;
	private String title;
	private String isbn;
	private String publisher;
	private double cost;
	private int quantity;

	public Book(String isbn) {
		this.isbn = isbn;
	}

	public Book(String title, String isbn, String publisher, double cost, int quantity) {
		this.title = title;
		this.isbn = isbn;
		this.publisher = publisher;
		this.cost = cost;
		this.quantity = quantity;
	}

	public String getTitle() {
		return title;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getPublisher() {
		return publisher;
	}

	public double getCost() {
		return cost;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "> " + this.title + ", " + this.isbn + ", " + this.publisher + " ($" + this.cost + "), Available: " + this.quantity;
	}
}
