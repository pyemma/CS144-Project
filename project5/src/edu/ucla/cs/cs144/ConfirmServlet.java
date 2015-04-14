package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ConfirmServlet extends HttpServlet implements Servlet{

	public ConfirmServlet() {}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
			if(request.isSecure() == false) {
				response.sendRedirect("error.html");
			}
			else {

				HttpSession session = request.getSession(true);

				HashMap<String, Item> items = (HashMap<String, Item>)session.getAttribute("items");
				String id = request.getParameter("id");
				String card = request.getParameter("card");

				if(items == null) {
					response.sendRedirect("error.html");
				}
				else if(items.containsKey(id) == false || card == null || card.equals("")) {
					response.sendRedirect("error.html");
				}
				else {
					Item item = items.get(id);
					String itemId = item.getItemId();
					String price = item.getBuyPrice();
					String itemName = item.getName();
					DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date date = new Date();
					String currentTime = format.format(date);
					// String serverName = request.getServerName();
					// String port = request.getServerPort();

					request.setAttribute("id", itemId);
					request.setAttribute("price", price);
					request.setAttribute("name", itemName);
					request.setAttribute("card", card);
					request.setAttribute("time", currentTime);

					items.remove(id);
					session.setAttribute("items", items);
					// session.invalidate	();

					request.getRequestDispatcher("confirm.jsp").forward(request, response);
				}
			}
		}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
			doGet(request, response);
		}
}