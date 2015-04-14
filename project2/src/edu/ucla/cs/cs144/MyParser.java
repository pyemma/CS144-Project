/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
    
        Element[] elems = getElementsByTagNameNR(doc.getDocumentElement(), "Item");
        for(Element e : elems) {

            String itemId = e.getAttribute("ItemID");
            String name = getElementTextByTagNameNR(e, "Name");
            String currently = strip(getElementTextByTagNameNR(e, "Currently"));
            String buy_price = strip(getElementTextByTagNameNR(e, "Buy_Price"));
            String first_bid = strip(getElementTextByTagNameNR(e, "First_Bid"));
            String number_of_bids = getElementTextByTagNameNR(e, "Number_of_Bids");
            String country = getElementTextByTagNameNR(e, "Country");
            String started = changeTime(getElementTextByTagNameNR(e, "Started"));
            String ends = changeTime(getElementTextByTagNameNR(e, "Ends"));
            Element seller = getElementByTagNameNR(e, "Seller");
            String sellerId = seller.getAttribute("UserID");
            String sellerRatings = seller.getAttribute("Rating");
            String location = getElementTextByTagNameNR(e, "Location");
            Element locate = getElementByTagNameNR(e, "Location");
            String latitude = locate.getAttribute("Latitude");
            String longitude = locate.getAttribute("Longitude");
            String description = getElementTextByTagNameNR(e, "Description");
            if(description.length() > 4000)
                description = description.substring(0, 4001);

            // add the record to the list
            items.add(itemRecord(itemId, name, currently, buy_price, first_bid, number_of_bids, country,
                started, ends, sellerId, location, latitude, longitude, description));

            // add the category infomation and belongsTo information
            Element[] cates = getElementsByTagNameNR(e, "Category");
            for(Element c : cates) {
                String cate = getElementText(c);
                if(category.containsKey(cate) == false) {
                    int cnt = category.size();
                    category.put(cate, cnt);
                }
                int index = category.get(cate);
                String caterecord = itemId + columnSeparator + index;
                belongsTo.add(caterecord);
            }

            // add the user information
            if(users.containsKey(sellerId)) {
                users.get(sellerId).sellRating = sellerRatings;
            }
            else {
                User u = new User(sellerId);
                u.sellRating = sellerRatings;
                users.put(sellerId, u);
            }

            // add the bid infromation and bider inforamtion
            Element[] ebids = getElementsByTagNameNR(getElementByTagNameNR(e, "Bids"), "Bid");
            for(Element b : ebids) {
                Element bidder = getElementByTagNameNR(b, "Bidder");
                String bidderId = bidder.getAttribute("UserID");
                String bidderRating = bidder.getAttribute("Rating");
                String bidderloc = getElementTextByTagNameNR(bidder, "Location");
                String biddercoun = getElementTextByTagNameNR(bidder, "Country");
                String amount = strip(getElementTextByTagNameNR(b, "Amount"));
                String time = changeTime(getElementTextByTagNameNR(b, "Time"));


                if(users.containsKey(bidderId) == false) {
                    User u = new User(bidderId);
                    users.put(bidderId, u);
                }
                users.get(bidderId).location = bidderloc;
                users.get(bidderId).country = biddercoun;
                users.get(bidderId).bidRating = bidderRating;
                bids.add(bidRecord(itemId, bidderId, time, amount));
            }

        }
        
        
        /**************************************************************/
        
    }

    private static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat format2 = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
    static String changeTime(String date) {
        try {
            Date d = format2.parse(date);
            return format1.format(d);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return "";
    }
    
    static String itemRecord(String itemID, String name, String currently, String buy_price,
        String first_bid, String number_of_bids, String country, String started, String ends,
        String sellerId, String location, String latitude, String longitude, String description) {

        StringBuffer record = new StringBuffer();
        // add the itemID
        record.append(itemID); record.append(columnSeparator); 
        // add the name
        record.append(name); record.append(columnSeparator);
        // add the currently
        record.append(currently); record.append(columnSeparator);
        // add the buy_price
        if(buy_price.equals("")) record.append("\\N");
        else record.append(buy_price);
        record.append(columnSeparator);
        // add the first_bid
        record.append(first_bid); record.append(columnSeparator);
        // add the number_of_bids
        record.append(number_of_bids); record.append(columnSeparator);
        // add the location
        record.append(location); record.append(columnSeparator);
        // add the latitude
        if(latitude.equals("")) record.append("\\N");
        else record.append(latitude); 
        record.append(columnSeparator);
        // add the longitude
        if(longitude.equals("")) record.append("\\N");
        else record.append(longitude); 
        record.append(columnSeparator);
        // add the country
        record.append(country); record.append(columnSeparator);
        // add started time
        record.append(started); record.append(columnSeparator);
        // add ends time
        record.append(ends); record.append(columnSeparator);
        // add sellerId
        record.append(sellerId); record.append(columnSeparator);
        // add description
        record.append(description);

        return record.toString();
    }

    static String bidRecord(String itemId, String userId, String time, String amount) {
        StringBuffer record = new StringBuffer();
        // add item information
        record.append(itemId); record.append(columnSeparator);
        // add user information
        record.append(userId); record.append(columnSeparator);
        // add time information
        record.append(time); record.append(columnSeparator);
        // add amount inforamtion
        record.append(amount);

        return record.toString();
    }

    private static class User {
        String userID, location, country;
        String sellRating, bidRating;
        public User() {
            userID = "";
            location = "";
            country = "";
            sellRating = "";
            bidRating = "";
        }
        public User(String userID, String location, String country) {
            this.userID = userID;
            this.location = location;
            this.country = country;
            sellRating = "";
            bidRating = "";
        }
        public User(String userID) {
            this.userID = userID;
            location = "";
            country = "";
            sellRating = "";
            bidRating = "";
        }

        public String toString() {
            StringBuffer record = new StringBuffer();
            record.append(userID); record.append(columnSeparator);
            if(location.equals("")) record.append("\\N");
            else record.append(location);
            record.append(columnSeparator);
            if(country.equals("")) record.append("\\N");
            else record.append(country);
            record.append(columnSeparator);
            if(sellRating.equals("")) record.append("\\N");
            else record.append(sellRating); 
            record.append(columnSeparator);
            if(bidRating.equals("")) record.append("\\N");
            else record.append(bidRating);
            return record.toString();
        }
    }

    private static HashSet<String> items;
    private static HashMap<String, Integer> category;
    private static HashMap<String, User> users;
    private static HashSet<String> belongsTo;
    private static ArrayList<String> bids;

    private static void writeToFile(){
        try {
            System.out.println("Start to write to file");
            BufferedWriter bwItems = new BufferedWriter(new FileWriter("items.txt"));
            BufferedWriter bwUsers = new BufferedWriter(new FileWriter("users.txt"));
            BufferedWriter bwCates = new BufferedWriter(new FileWriter("categorys.txt"));
            BufferedWriter bwBids = new BufferedWriter(new FileWriter("bids.txt"));
            BufferedWriter bwBelong = new BufferedWriter(new FileWriter("belongsTo.txt"));

            // write the user information
            for(String str : users.keySet()) {
                String record = users.get(str).toString();
                bwUsers.write(record);
                bwUsers.newLine();
            }

            // write the item information
            for(String str : items) {
                bwItems.write(str);
                bwItems.newLine();
            }

            // write the category information
            for(String str : category.keySet()) {
                int index = category.get(str);
                String record = index + columnSeparator + str;
                bwCates.write(record);
                bwCates.newLine();
            }

            // write the belongsTo information
            for(String str : belongsTo) {
                bwBelong.write(str);
                bwBelong.newLine();
            }

            // write the bids information
            for(String str : bids) {
                bwBids.write(str);
                bwBids.newLine();
            }

            bwItems.close();
            bwUsers.close();
            bwCates.close();
            bwBids.close();
            bwBelong.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        items = new HashSet<String>();
        category = new HashMap<String, Integer>();
        users = new HashMap<String, User>();
        belongsTo = new HashSet<String>();
        bids = new ArrayList<String>();
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
        writeToFile();
    }
}
