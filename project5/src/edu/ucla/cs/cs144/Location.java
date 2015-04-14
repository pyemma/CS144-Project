package edu.ucla.cs.cs144;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlValue;

import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {})
public class Location {
	@XmlAttribute(name = "Latitude")
	private String Latitude;
	@XmlAttribute(name = "Longitude")
	private String Longitude;


	@XmlValue
	private String Location;

	public String getLocation() {
		return this.Location;
	}
	
	public void setLocation(String location) {
		this.Location = location;
	}


	// @XmlAttribute(name = "Latitude")
	public String getLatitude() {
		return this.Latitude;
	}

	public void setLatitude(String latitude) {
		this.Latitude = latitude;
	}

	// @XmlAttribute(name = "Longitude")
	public String getLongitude() {
		return this.Longitude;
	}

	public void setLongitude(String longitude) {
		this.Longitude = longitude;
	}
}