package edu.ucla.cs.cs144;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.util.*;

/*
	Class for mapping xml document to Bidder object
*/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {})
public class Bidder {

	private String Rating;
	private String UserID;
	private String Location;
	private String Country;

	@XmlAttribute(name = "Rating")
	public String getBidderRating() {
		return this.Rating;
	}

	public void setBidderRating(String rating) {
		this.Rating = rating;
	}
	@XmlAttribute(name = "UserID")
	public String getBidderId() {
		return this.UserID;
	}

	public void setBidderId(String userId) {
		this.UserID = userId;
	}
	@XmlElement(name = "Location")
	public String getLocation() {
		return this.Location;
	}
	
	public void setLocation(String location) {
		this.Location = location;
	}
	@XmlElement(name = "Country")
	public String getCountry() {
		return this.Country;
	}

	public void setCountry(String country) {
		this.Country = country;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\tName: "); sb.append(this.UserID); sb.append("\n");
		sb.append("\tRating: "); sb.append(this.Rating); sb.append("\n");
		sb.append("\tLocation: "); sb.append(this.Location); sb.append("\n");
		sb.append("\tCountry: "); sb.append(this.Country); sb.append("\n");
		return sb.toString();
	}
}