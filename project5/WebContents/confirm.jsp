<html>
<head>
	<title> Thank you for your payment </title>
</head>
<body>
	<h5> Bill information </h5>
	<ul>
		<li>Item Id: <%= request.getAttribute("id") %></li>
		<li>Item Name: <%= request.getAttribute("name") %></li>
		<li>Buy Price: <%= request.getAttribute("price") %></li>
		<li>Credit Card: <%= request.getAttribute("card") %></li>
		<li>Time: <%= request.getAttribute("time") %></li>
	</ul>
	<br>
	<br>
	<h5> Thank you for your payment! </h5>
	<br>
	<br>
	<ul>
		<% 	String keywordsearch = "http://" + request.getServerName() + ":1448" + request.getContextPath() + "/keywordSearch.html";
			String getitem = "http://" + request.getServerName() + ":1448" + request.getContextPath() + "/getItem.html"; %>
		<li><a href = "<%= keywordsearch %>">Click here to return to the keyword search page.</a></li>
		<li><a href = "<%= getitem %>">Click here to return to the item search page.</a></li>
	</ul>
</body>
</html>