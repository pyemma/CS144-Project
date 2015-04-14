package edu.ucla.cs.cs144;

public class Confrim {
	
	private final String itemId;
	private final String itemName;
	private final String price;
	private final String card;

	public Confrim() {
		itemId = null;
		itemName = null;
		price = null;
		card = null;
	}

	public Confrim(String itemId, String itemName, String price, String card) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.price = price;
		this.card = card;
	}

}