package me.mindlessly.notenoughcoins.utils;

public class Auction {
	private String name;
	private String id;
	private String tier;
	private double price;

	public Auction(String name, String id, double price, String tier) {
		this.name = name;
		this.id = id;
		this.price = price;
		this.tier = tier;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public String getTier() {
		return tier;
	}

	public double getPrice() {
		return price;
	}

}
