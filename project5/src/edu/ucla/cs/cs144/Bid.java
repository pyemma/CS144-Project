package edu.ucla.cs.cs144;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.util.*;

/*
	Class for mapping xml document to Bid class
*/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {})
public class Bid {
	private Bidder Bidder;
	private String Time;
	private String Amount;

	@XmlElement(name = "Bidder")
	public Bidder getBidder() {
		return this.Bidder;
	}

	public void setBidder(Bidder bidder) {
		this.Bidder = bidder;
	}
	@XmlElement(name = "Time")
	public String getTime() {
		return this.Time;
	}

	public void setTime(String time) {
		this.Time = time;
	}
	@XmlElement(name = "Amount")
	public String getAmount() {
		return this.Amount;
	}

	public void setAmount(String amount) {
		this.Amount = amount;
	}

	// Depracted, not used in the latest version
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Bidder Information:\n");
		sb.append(Bidder.toString());
		sb.append("Time: "); sb.append(this.Time); sb.append("\n");
		sb.append("Amount: "); sb.append(this.Amount); sb.append("\n");
		return sb.toString();
	}
}