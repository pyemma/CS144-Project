package edu.ucla.cs.cs144;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElementWrapper;

import java.util.*;

/*
	Class for mapping from xml document to Item object
*/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Item")
@XmlType(propOrder = {})
public class Item {
	private String ItemID;
	private String Name;
	private List<String> Category = new ArrayList<String>();
	private String Currently;
	private String Buy_Price;
	private String First_Bid;
	private Integer Number_of_Bids;
	private List<Bid> Bid = new ArrayList<Bid>();
	private Location Location;
	// private String Latitude;
	// private String Longitude;
	private String Country;
	private String Started;
	private String Ends;
	// private String UserID;
	// private String Rating;
	private Seller Seller;
	private String Description;

	@XmlAttribute(name = "ItemID")
	public String getItemId() {
		return this.ItemID;
	}

	public void setItemId(String itemId) {
		this.ItemID = itemId;
	}
	@XmlElement(name = "Name")
	public String getName() {
		return this.Name;
	}

	public void setName(String name) {
		this.Name = Name;
	}
	@XmlElement(name = "Category")
	public List<String> getCategories() {
		return this.Category;
	}

	public void setCategories(List<String> categories) {
		this.Category = categories;
	}	
	// @XmlElement(name = "Currently")
	public String getCurrently() {
		return this.Currently;
	}

	public void setCurrently(String currently) {
		this.Currently = currently;
	}
	@XmlElement(name = "Buy_Price")
	public String getBuyPrice() {
		return this.Buy_Price;
	}

	public void setBuyPrice(String buyPrice) {
		this.Buy_Price = buyPrice;
	}
	@XmlElement(name = "First_Bid") 
	public String getFirstBid() {
		return this.First_Bid;
	}

	public void setFirstBid(String firstBid) {
		this.First_Bid = firstBid;
	}
	@XmlElement(name = "Number_of_Bids")
	public int getNumberOfBids() {
		return this.Number_of_Bids;
	}

	public void setNumberOfBids(Integer numberOfBids) {
		this.Number_of_Bids = numberOfBids;
	}

	@XmlElementWrapper(name = "Bids")
	@XmlElement(name = "Bid")
	public List<Bid> getBids() {
		return this.Bid;
	}

	public void setBids(List<Bid> bids) {
		this.Bid = bids;
	}

	public Location getLocation() {
		return this.Location;
	}
	@XmlElement(name = "Location")
	public void setLocation(Location location) {
		this.Location = location;
	}

	// public String getLatitude() {
	// 	return this.Latitude;
	// }
	// @XmlAttribute(name = "Latitude")
	// public void setLatitude(String latitude) {
	// 	this.Latitude = latitude;
	// }
	// @XmlAttribute(name = "Longitude")
	// public String getLongitude() {
	// 	return this.Longitude;
	// }

	// public void setLongitude(String longitude) {
	// 	this.Longitude = longitude;
	// }
	@XmlElement(name = "Country")
	public String getCountry() {
		return this.Country;
	}

	public void setCountry(String country) {
		this.Country = country;
	}

	public String getStarted() {
		return this.Started;
	}
	@XmlElement(name = "Started")
	public void setStarted(String started) {
		this.Started = started;
	}
	@XmlElement(name = "Ends")
	public String getEnds() {
		return this.Ends;
	}

	public void setEnds(String ends) {
		this.Ends = ends;
	}
	// @XmlAttribute(name = "UserID")
	// public String getUserID() {
	// 	return this.UserID;
	// }
	// public void setUserID(String userId) {
	// 	this.UserID = userId;
	// }

	// @XmlAttribute(name = "Rating")
	// public String getRating() {
	// 	return this.Rating;
	// }
	// public void setRating(String rating) {
	// 	this.Rating = rating;
	// } 
	@XmlElement(name = "Seller")
	public Seller getSeller() {
		return this.Seller;
	}

	public void setSeller(Seller seller) {
		this.Seller = seller;
	} 
	
	@XmlElement(name = "Description")
	public String getDescription() {
		return this.Description;
	}

	public void setDescription(String description) {
		this.Description = description;
	}
}

