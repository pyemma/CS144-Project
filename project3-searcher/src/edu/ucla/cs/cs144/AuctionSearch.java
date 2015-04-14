package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.*;
 
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	private IndexSearcher searcher = null;
	private QueryParser parser = null;

	private void getSearcherParser() throws IOException, ParseException {
		if(searcher == null) {
			String filepath = "/var/lib/lucene/";
			searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(filepath))));
		}
		if(parser == null) {
			parser = new QueryParser("content", new StandardAnalyzer());
		}
	}

	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		// TODO: Your code here!
		SearchResult[] result = new SearchResult[0];
		// Invalid input
		if(numResultsToSkip + numResultsToReturn <= 0 || numResultsToSkip < 0 || numResultsToReturn < 0)
			return result;
		try {
			getSearcherParser();
			Query qu = parser.parse(query);
			TopDocs docs = searcher.search(qu, numResultsToSkip+numResultsToReturn);
			ScoreDoc[] hits = docs.scoreDocs;
			result = new SearchResult[Math.max(hits.length - numResultsToSkip, 0)];
			for(int i = numResultsToSkip; i < hits.length; i++) {
				Document doc = searcher.doc(hits[i].doc);
				SearchResult sr = new SearchResult();
				sr.setItemId(doc.get("id"));
				sr.setName(doc.get("name"));
				result[i - numResultsToSkip] = sr;
			}
		} catch (IOException ex) {
			System.out.println(ex);
		} catch (ParseException ex) {
			System.out.println(ex);
		}
		return result;
	}

	/* get the string format of the region */
	private String getLocation(SearchRegion region) {
		StringBuilder sb = new StringBuilder();
		sb.append("GeomFromText('Polygon((");
		sb.append(region.getLx()); sb.append(" "); sb.append(region.getLy()); sb.append(",");
		sb.append(region.getLx()); sb.append(" "); sb.append(region.getRy()); sb.append(",");
		sb.append(region.getRx()); sb.append(" "); sb.append(region.getRy()); sb.append(",");
		sb.append(region.getRx()); sb.append(" "); sb.append(region.getLy()); sb.append(",");
		sb.append(region.getLx()); sb.append(" "); sb.append(region.getLy()); sb.append("))')");
		return sb.toString();

	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		SearchResult[] result = new SearchResult[0];
		if(numResultsToSkip + numResultsToReturn <= 0 || numResultsToSkip < 0 || numResultsToReturn < 0)
			return result;

		/* create a database connection */
		Connection conn = null;

		try {
		    conn = DbManager.getConnection(true);
		} catch (SQLException ex) {
		    System.out.println(ex);
		}
		HashSet<String> items = new HashSet<String>();
		/* query using the spatial index */
		try {
			Statement s = conn.createStatement();
			StringBuilder qu = new StringBuilder();
			qu.append("SELECT ItemID FROM Geom WHERE MBRContains(");
			qu.append(getLocation(region));
			qu.append(", Location)");
			
			ResultSet rs = s.executeQuery(qu.toString());
		
			while(rs.next()) {
				String id = rs.getString("ItemID");
				items.add(id);
			}
			
			rs.close();
			s.close();

		} catch (SQLException ex) {
			System.out.println(ex);
		}

		List<SearchResult> list = new ArrayList<SearchResult>();
		/* query using the Lucene index */
		try {
			getSearcherParser();
			Query qu = parser.parse(query);
			TopDocs docs = searcher.search(qu, Integer.MAX_VALUE);
			ScoreDoc[] hits = docs.scoreDocs;
			
			for(int i = 0; i < hits.length; i++) {
				Document doc = searcher.doc(hits[i].doc);
				if(items.contains(doc.get("id"))) {
					SearchResult sr = new SearchResult();
					sr.setItemId(doc.get("id"));
					sr.setName(doc.get("name"));
					list.add(sr);
				}
			}
		} catch (IOException ex) {
			System.out.println(ex);
		} catch (ParseException ex) {
			System.out.println(ex);
		}
		result = new SearchResult[Math.min(numResultsToReturn, Math.max(list.size() - numResultsToSkip, 0))];
		for(int i = 0; i < result.length; i++) {
			result[i] = list.get(i + numResultsToSkip);
		}

		try {
			conn.close();
		} catch (SQLException ex) {
			System.out.println(ex);
		}

		return result;
	}

	/* helper function to change the format of the time */
	private static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat format2 = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
	private static String changeTime(String date) {
		try {
			Date d = format1.parse(date);
			return format2.format(d);
		} catch(Exception ex) {
			System.out.println(ex);
		}
		return "";
	}

	// public String getXMLDataForItemId(String itemId) {
	// 	// TODO: Your code here!
	// 	Connection conn = null;

	// 	try {
	// 		conn = DbManager.getConnection(true);
	// 	} catch (SQLException ex) {
	// 		System.out.println(ex);
	// 	}

	// 	StringBuilder item = new StringBuilder();

	// 	try {

	// 		PreparedStatement ps = conn.prepareStatement(
	// 			"SELECT * FROM Item WHERE ItemID = ?"
	// 			);
	// 		ps.setString(1, itemId);
	// 		ResultSet rs = ps.executeQuery();
	// 		PreparedStatement psc = conn.prepareStatement(
	// 			"SELECT * FROM Category, BelongsTo WHERE Cid = CateId AND Iid = ?"
	// 			);
	// 		psc.setString(1, itemId);
	// 		ResultSet rsc = psc.executeQuery();
	// 		PreparedStatement psb = conn.prepareStatement(
	// 			"SELECT * FROM BidsInfo, AcutionUser WHERE Uid = UserID and Iid = ?"
	// 			);
	// 		psb.setString(1, itemId);
	// 		ResultSet rsb = psb.executeQuery();

			
	// 		while(rs.next()) {
	// 			/* add the ItemID */
	// 			item.append("<Item ItemID=\"");
	// 			item.append(rs.getString("ItemID"));
	// 			item.append("\">\n");
	// 			/* add the name */
	// 			item.append("\t<Name>");
	// 			item.append(rs.getString("Name"));
	// 			item.append("</Name>\n");
	// 			/* add the category */
	// 			while(rsc.next()) {
	// 				item.append("\t<Category>");
	// 				item.append(rsc.getString("Type"));
	// 				item.append("</Category>\n");
	// 			}
	// 			/* add the current */
	// 			item.append("\t<Currently>$");
	// 			item.append(rs.getFloat("Currently"));
	// 			item.append("</Currently>\n");
	// 			/* add the buy price if exist */
	// 			if(rs.getFloat("Buy_Price") != 0) {
	// 				item.append("\t<Buy_Price>$");
	// 				item.append(rs.getFloat("Buy_Price"));
	// 				item.append("</Buy_Price>\n");
	// 			}
	// 			/* add the first bid */
	// 			item.append("\t<First_Bid>$");
	// 			item.append(rs.getFloat("First_Bid"));
	// 			item.append("</First_Bid>\n");
	// 			/* add the number of bids */
	// 			item.append("\t<Number_of_Bids>");
	// 			item.append(rs.getInt("Number_of_Bids"));
	// 			item.append("</Number_of_Bids>\n");
	// 			/* add the bids */
	// 			if(rs.getInt("Number_of_Bids") == 0) {
	// 				item.append("\t<Bids />\n");
	// 			}
	// 			else {
	// 				item.append("\t<Bids>\n");
	// 				while(rsb.next()) {
	// 					item.append("\t\t<Bid>\n");
	// 					item.append("\t\t\t<Bidder Rating=\"");
	// 					item.append(rsb.getString("SellRating"));
	// 					item.append("\" UserID=\"");
	// 					item.append(rsb.getString("UserID"));
	// 					item.append("\">\n");
	// 					item.append("\t\t\t\t<Location>");
	// 					item.append(rsb.getString("Location"));
	// 					item.append("</Location>\n");
	// 					item.append("\t\t\t\t<Country>");
	// 					item.append(rsb.getString("Country"));
	// 					item.append("</Country>\n");
	// 					item.append("\t\t\t</Bidder>\n");
	// 					item.append("\t\t\t<Time>");
	// 					item.append(changeTime(rsb.getString("Time")));
	// 					item.append("</Time>\n");
	// 					item.append("\t\t\t<Amount>$");
	// 					item.append(rsb.getFloat("Amount"));
	// 					item.append("</Amount>\n");
	// 					item.append("\t\t</Bid>\n");
	// 				}
	// 				item.append("\t</Bids>\n");
	// 			}
	// 			/* add the location */
	// 			if(rs.getString("Latitude") != null &&
	// 				rs.getString("Longitude") != null) {
	// 				item.append("\t<Location Latitude=\"");
	// 				item.append(rs.getString("Latitude"));
	// 				item.append("\" Longitude=\"");
	// 				item.append(rs.getString("Longitude"));
	// 				item.append("\">");
	// 				item.append(rs.getString("Location"));
	// 				item.append("</Location>\n");
	// 			}
	// 			else {
	// 				item.append("\t<Location>");
	// 				item.append(rs.getString("Location"));
	// 				item.append("</Location>\n");
	// 			}
	// 			/* add the country */
	// 			item.append("\t<Country>");
	// 			item.append(rs.getString("Country"));
	// 			item.append("</Country>\n");
	// 			/* add the started */
	// 			item.append("\t<Started>");
	// 			item.append(changeTime(rs.getString("Started")));
	// 			item.append("</Started>\n");
	// 			/* add the ends */
	// 			item.append("\t<Ends>");
	// 			item.append(changeTime(rs.getString("Ends")));
	// 			item.append("</Ends>\n");
	// 			/* add the Seller */
	// 			Statement s = conn.createStatement();
	// 			ResultSet seller = s.executeQuery("SELECT * FROM AcutionUser WHERE UserID = \"" + rs.getString("Seller") + "\"");
	// 			while(seller.next()) {
	// 				item.append("\t<Seller Rating=\"");
	// 				item.append(seller.getString("SellRating"));
	// 				item.append("\" UserID=\"");
	// 				item.append(seller.getString("UserID"));
	// 				item.append("\" />\n");
	// 			}
	// 			/* add the description */
	// 			item.append("\t<Description>");
	// 			item.append(rs.getString("Description"));
	// 			item.append("</Description>\n");
	// 			item.append("</Item>\n");
	// 		}

	// 	} catch (SQLException ex) {
	// 		System.out.println(ex);
	// 	}	
	// 	return item.toString();
	// }
	
	public String getXMLDataForItemId(String itemId) {
		Connection conn = null;
		StreamResult result = new StreamResult(new StringWriter());

		try {
			conn = DbManager.getConnection(true);
		} catch (SQLException ex) {
			System.out.println(ex);
		}

		try {

			PreparedStatement ps = conn.prepareStatement(
				"SELECT * FROM Item WHERE ItemID = ?"
				);
			ps.setString(1, itemId);
			ResultSet rs = ps.executeQuery();
			PreparedStatement psc = conn.prepareStatement(
				"SELECT * FROM Category, BelongsTo WHERE Cid = CateId AND Iid = ?"
				);
			psc.setString(1, itemId);
			ResultSet rsc = psc.executeQuery();
			PreparedStatement psb = conn.prepareStatement(
				"SELECT * FROM BidsInfo, AcutionUser WHERE Uid = UserID and Iid = ? ORDER BY Time"
				);
			psb.setString(1, itemId);
			ResultSet rsb = psb.executeQuery();

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			org.w3c.dom.Document doc = docBuilder.newDocument();
			
			while(rs.next()) {
				Element rootElement = doc.createElement("Item");
				doc.appendChild(rootElement);
				/* add the ItemID */
				Attr attr = doc.createAttribute("ItemID");
				attr.setValue(rs.getString("ItemID"));
				rootElement.setAttributeNode(attr);
				/* add the name */
				Element name = doc.createElement("Name");
				name.appendChild(doc.createTextNode(rs.getString("Name")));
				rootElement.appendChild(name);
				/* add the category */
				while(rsc.next()) {
					Element cate = doc.createElement("Category");
					cate.appendChild(doc.createTextNode(rsc.getString("Type")));
					rootElement.appendChild(cate);
				}
				/* add the current */
				Element current = doc.createElement("Currently");
				current.appendChild(doc.createTextNode("$" + String.format("%.2f", rs.getFloat("Currently"))));
				rootElement.appendChild(current);
				/* add the buy price if exist */
				if(rs.getFloat("Buy_Price") != 0) {
					Element buy = doc.createElement("Buy_Price");
					buy.appendChild(doc.createTextNode("$" + String.format("%.2f", rs.getFloat("Buy_Price"))));
					rootElement.appendChild(buy);
				}
				/* add the first bid */
				Element first = doc.createElement("First_Bid");
				first.appendChild(doc.createTextNode("$" + String.format("%.2f", rs.getFloat("First_Bid"))));
				rootElement.appendChild(first);
				/* add the number of bids */
				Element numberOfBids = doc.createElement("Number_of_Bids");
				numberOfBids.appendChild(doc.createTextNode("" + rs.getInt("Number_of_Bids")));
				rootElement.appendChild(numberOfBids);
				/* add the bids */
				Element bids = doc.createElement("Bids");
				rootElement.appendChild(bids);
				while(rsb.next()) {
					Element bid = doc.createElement("Bid");
					bids.appendChild(bid);
					Element bidder = doc.createElement("Bidder");
					bid.appendChild(bidder);
					Attr rate = doc.createAttribute("Rating");
					rate.setValue(rsb.getString("BidRating"));
					bidder.setAttributeNode(rate);
					Attr userID = doc.createAttribute("UserID");
					userID.setValue(rsb.getString("UserID"));
					bidder.setAttributeNode(userID);
					Element local = doc.createElement("Location");
					local.appendChild(doc.createTextNode(rsb.getString("Location")));
					bidder.appendChild(local);
					Element country = doc.createElement("Country");
					country.appendChild(doc.createTextNode(rsb.getString("Country")));
					bidder.appendChild(country);
					Element time = doc.createElement("Time");
					time.appendChild(doc.createTextNode(changeTime(rsb.getString("Time"))));
					bid.appendChild(time);
					Element amount = doc.createElement("Amount");
					amount.appendChild(doc.createTextNode("$" + String.format("%.2f", rsb.getFloat("Amount"))));
					bid.appendChild(amount);
				}
				Element local = doc.createElement("Location");
				rootElement.appendChild(local);
				local.appendChild(doc.createTextNode(rs.getString("Location")));	
				/* add the location */
				if(rs.getString("Latitude") != null &&
					rs.getString("Longitude") != null) {
					Attr lati = doc.createAttribute("Latitude");
					lati.setValue(rs.getString("Latitude"));
					local.setAttributeNode(lati);
					Attr loni = doc.createAttribute("Longitude");
					loni.setValue(rs.getString("Longitude"));
					local.setAttributeNode(loni);

				}
				/* add the country */
				Element country = doc.createElement("Country");
				country.appendChild(doc.createTextNode(rs.getString("Country")));
				rootElement.appendChild(country);
				/* add the started */
				Element start = doc.createElement("Started");
				start.appendChild(doc.createTextNode(changeTime(rs.getString("Started"))));
				rootElement.appendChild(start);
				/* add the ends */
				Element ends = doc.createElement("Ends");
				ends.appendChild(doc.createTextNode(changeTime(rs.getString("Ends"))));
				rootElement.appendChild(ends);
				/* add the Seller */
				Statement s = conn.createStatement();
				ResultSet seller = s.executeQuery("SELECT * FROM AcutionUser WHERE UserID = \"" + rs.getString("Seller") + "\"");
				while(seller.next()) {
					Element sell = doc.createElement("Seller");
					Attr rate = doc.createAttribute("Rating");
					rate.setValue(seller.getString("SellRating"));
					sell.setAttributeNode(rate);
					Attr userID = doc.createAttribute("UserID");
					userID.setValue(seller.getString("UserID"));
					sell.setAttributeNode(userID);
					rootElement.appendChild(sell);
				}
				/* add the description */
				Element desc = doc.createElement("Description");
				desc.appendChild(doc.createTextNode(rs.getString("Description")));
				rootElement.appendChild(desc);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			DOMSource source = new DOMSource(doc);

			// StreamResult result = new StreamResult(new StringWriter());
			transformer.transform(source, result);

			rs.close();
			rsc.close();
			rsb.close();

		} catch (Exception ex) {
			System.out.println(ex);
		}

		try {
			conn.close();
		} catch (SQLException ex) {
			System.out.println(ex);
		}

		return result.getWriter().toString();

	}

	public String echo(String message) {
		return message;
	}

}
