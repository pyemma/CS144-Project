package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        AuctionSearchClient client = new AuctionSearchClient();
        String query = request.getParameter("q");
        query = query.trim();
        int numResultsToSkip = 0;
        int numResultsToReturn = 20;
        try {
            numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
        }
        catch (Exception e) {
            numResultsToSkip = 0;
        }
        try {
            numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
        }
        catch (Exception e) {
            numResultsToReturn = 20;
        }
        /* 
            Invalid input, set it to a valid default number.
            If the number of results to return is set to 0, then return all the result.
        */
        if(numResultsToSkip < 0)
            numResultsToSkip = 0;
        if(numResultsToReturn < 0)
            numResultsToReturn = 20;
        else if(numResultsToReturn == 0)
            numResultsToReturn = 1000000;

        SearchResult[] result = client.basicSearch(query, numResultsToSkip, numResultsToReturn);
        SearchResult[] furture = client.basicSearch(query, numResultsToSkip+numResultsToReturn, numResultsToReturn);
        
        request.setAttribute("result", result);
        request.setAttribute("query", URLEncoder.encode(query, "UTF-8"));
        request.setAttribute("pre", Math.max(0, numResultsToSkip - numResultsToReturn));
        request.setAttribute("next",  numResultsToSkip + numResultsToReturn);
        request.setAttribute("return", numResultsToReturn);

        // Enable and disable the navigate link
        if(numResultsToSkip == 0)
            request.setAttribute("displayPre", "visibility : hidden");
        else
            request.setAttribute("disablePre", "");

        if(furture.length <= 0)
            request.setAttribute("displayNext", "visibility : hidden");
        else
            request.setAttribute("displayNext", "");

        request.getRequestDispatcher("keywordSearchResult.jsp").forward(request, response);
    }
}
