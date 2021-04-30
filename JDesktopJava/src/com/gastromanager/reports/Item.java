package com.gastromanager.reports;

public class Item {//implements Comparable<String>{
	private String name;
	private Integer quantity;
	private Double price;
	private String itemId;
	
	public Item(String name, Integer quantity, Double price, String itemId) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.itemId = itemId;
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
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o.getClass() != this.getClass()) {
			return false;
		}
		final Item item = (Item) o;
		if (this.itemId.equals(item.itemId)) {
			return true;
		}
		return false;
	}
}
