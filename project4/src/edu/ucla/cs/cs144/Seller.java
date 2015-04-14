package edu.ucla.cs.cs144;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElementWrapper;

import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {})
public class Seller {
	private String Rating;
	private String UserID;

	@XmlAttribute(name = "Rating")
	public String getRating() {
		return this.Rating;
	}

	public void setRating(String rating) {
		this.Rating = rating;
	}

	@XmlAttribute(name = "UserID")
	public String getUserID() {
		return this.UserID;
	}

	public void setUserID(String userId) {
		this.UserID = userId;
	}
}