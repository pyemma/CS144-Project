<html>
<head>
<title> Payment for the item <%= request.getAttribute("id") %></title>
</head>
<body>
	<h3> The basic infromation of the item <%= request.getAttribute("id") %> </h3>
	<ul>
		<li>Item Id: <%= request.getAttribute("id") %></li>
		<li>Item Name: <%= request.getAttribute("name") %></li>
		<li>Buy Price: <%= request.getAttribute("price") %></li>
	</ul>
	<form action="<%= request.getAttribute("link") %>" method="POST">
		<p> Credit Card Number: </p> &nbsp; &nbsp;
		<input type="text" name="card">
		<br>
		<br>
		<input type="submit" value="Submit">
	</form>
</body>
</html>

