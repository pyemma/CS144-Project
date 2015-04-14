package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    private final String urlString = "http://google.com/complete/search?output=toolbar&q=";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        String query = request.getParameter("input");
        URL url = new URL(urlString + URLEncoder.encode(query, "UTF-8"));
        URLConnection urlConnection = url.openConnection();
        HttpURLConnection httpConnection = (HttpURLConnection)urlConnection;

        InputStream input = httpConnection.getInputStream();
        InputStreamReader reader = new InputStreamReader(input);

        BufferedReader br = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();

        String line = null;
        while((line = br.readLine()) != null) {
        	sb.append(line);
        }

        response.setContentType("text/xml");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(sb.toString());
    }
}
