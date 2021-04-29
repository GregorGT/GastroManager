package com.gastromanager.reports;

public class Items implements Comparable<String>{
	private String name;
	private Integer quantity;
	private Double price;
	public Items(String name) {
		super();
		this.name = name;
		this.quantity = 0;
		this.price = 0.0;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o.getClass() != this.getClass()) {
			return false;
		}
		final Items items = (Items) o;
		if (this.name.equals(items.name)) {
			return true;
		}
		return false;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name==null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public int compareTo(String name) {
		if (this.name.equals(name)) {
			return 0;
		}
		if (this.name.compareTo(name) < 0) {
			return -1;
		}
		return 1;
	}
	
}
