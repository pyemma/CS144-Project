package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.transform.stream.StreamSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.net.URLEncoder;

import java.util.*;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        AuctionSearchClient client = new AuctionSearchClient();
        String query = request.getParameter("id");
        String xml = client.getXMLDataForItemId(query);
        // StringBuilder header = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        // xml = header.append(xml).toString();
        
        if(xml == null)
            response.sendRedirect("error.html");
        else {

            /* Convert to Item object */
            Item item = null;
            try {
    	        JAXBContext context = JAXBContext.newInstance(Item.class);
    	        Unmarshaller unmarshaller = context.createUnmarshaller();
    	        item = (Item)unmarshaller.unmarshal(new StreamSource(new StringReader(xml)));
    	    } catch (Exception e) {
                response.getWriter().write(e.toString());
    	    	System.out.println(e);
    	    } 

            // Handle the error if the item does not exist
            if(item == null) {
                response.sendRedirect("error.html");
            }
            else {

                List<Bid> bids = item.getBids();
                Collections.sort(bids, new Comparator<Bid>() {
                    public int compare(Bid bid1, Bid bid2) {
                        Date date1 = new Date(bid1.getTime());
                        Date date2 = new Date(bid2.getTime());
                        return -date1.compareTo(date2);
                    }
                });
                item.setBids(bids);


        	    request.setAttribute("item", item);
        	    request.setAttribute("query", query);
        	    request.setAttribute("xml", xml);
        	    
        	    request.getRequestDispatcher("itemResult.jsp").forward(request, response);
            }
        }
    }
}
