package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.*;

public class SessionServlet extends HttpServlet implements Servlet{

	public SessionServlet() {}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
			HttpSession session = request.getSession(true);

			HashMap<String, Item> items  = (HashMap<String, Item>)session.getAttribute("items");
			String id = request.getParameter("id");
			if(items == null) {
				response.sendRedirect("error.html");
			}
			else if(items.containsKey(id) == false) {
				response.sendRedirect("error.html");
			}
			else {
				Item item = items.get(id);
				String itemId = item.getItemId();
				String price = item.getBuyPrice();
				String itemName = item.getName();

				String serverName = request.getServerName();
				String context = request.getContextPath();
				String link = "https://" + serverName + ":8443" + context + "/confirm?id=" + itemId;

				request.setAttribute("id", itemId);
				request.setAttribute("price", price);
				request.setAttribute("name", itemName);
				request.setAttribute("link", link);

				session.setAttribute("items", items);

				request.getRequestDispatcher("payment.jsp").forward(request, response);
			}
		}
}