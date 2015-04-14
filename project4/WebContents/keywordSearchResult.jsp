<%@ page import="edu.ucla.cs.cs144.SearchResult;" %>
<html>
<head>
	<title>The results for <%= request.getAttribute("query") %></title>
	<script type="text/javascript" src="autosuggest.js"></script>
	<script type="text/javascript" src="suggestions.js"></script>
	<link rel="stylesheet" type="text/css" href="autosuggest.css" />
	<link rel="stylesheet" type="text/css" href="simple.css" />  
	<script type="text/javascript"> 
		window.onload = function () {
			var oTextbox = new AutoSuggestControl(document.getElementById("text1"), new StateSuggestions());
		}
	</script>	
</head>
<body>
<form action="search" method="GET">
	<h5> Please type in the keyword you want to search! </h5>
	<input type="text" id = "text1" name="q">
	<br>
	<input type = "hidden" name = "numResultsToSkip" value = "0" readonly>
	<br>
	<input type = "hidden" name = "numResultsToReturn" value = "20" readonly>
	<br>
	<input type="submit" value="Search">
</form>

<table border="1" width = "100%">
	<tr>
		<th width = "20%">ItemId</th>
		<th width = "80%">Name</th>
	</tr>
	<% 	SearchResult[] result = (SearchResult[])request.getAttribute("result");
		for(int i = 0; i < result.length; i++) { %>
		<tr>
			<td><a href = <%="item?id=" + result[i].getItemId() %>><%= result[i].getItemId() %></a></td>
			<td><%= result[i].getName() %></td>
		</tr>
	<% } %>
</table>

<p><span id = "searchresult"></span></p>

<table width = "100%">
	<tr>
		<td align="left"><a href = <%="search?q=" + request.getAttribute("query") + "&numResultsToSkip=" + request.getAttribute("pre") + "&numResultsToReturn=" + request.getAttribute("return") %> style = <%= request.getAttribute("displayPre") %>>Previous</a>
		</td>
		<td align="right"><a href = <%="search?q=" + request.getAttribute("query") + "&numResultsToSkip=" + request.getAttribute("next") + "&numResultsToReturn=" + request.getAttribute("return")%> style = <%= request.getAttribute("displayNext") %> >Next</a>
		</td>
</body>
</html>