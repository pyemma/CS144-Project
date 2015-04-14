<%@ page import = "java.util.*" %>
<%@ page import = "edu.ucla.cs.cs144.*;" %>
<html>
<head>
	<title> Result for <%= request.getAttribute("query") %></title>


	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	<link rel="stylesheet" type="text/css" href="simple.css" />  
	<style type="text/css">
	  html { height: 100% }
	  body { height: 100%; margin: 0px; padding: 0px }
	  #map_canvas { height: 100% }
	</style>
	<script type="text/javascript"
	    src="http://maps.google.com/maps/api/js?sensor=false">
	</script>
	<% Item item = (Item)request.getAttribute("item");
	%>
	<script type="text/javascript">
	  function initialize() {

	  	var draw = function(latlng) {
		    var myOptions = {
		      zoom: 14, // default is 8 
		      center: latlng,
		      mapTypeId: google.maps.MapTypeId.ROADMAP
		    };
		    var title = "<%= item.getName() %>";
		    var map = new google.maps.Map(document.getElementById("map_canvas"),
		        myOptions);
		    var marker = new google.maps.Marker({
		    	position: latlng,
		    	map: map,
		    	title: title
		    });
		}

	  	var latitude = <%= item.getLocation().getLatitude() %>;
	  	var longitude = <%= item.getLocation().getLongitude() %>;
	  	var latlng = null;

	  	if(latitude < -90 || latitude > 90)
	  		latitude = null;
	  	if(longitude < -180 || longitude > 180)
	  		longitude = null;
	  	
	  	if(latitude != null && longitude != null) {
	    	latlng = new google.maps.LatLng(latitude,longitude);
	    	draw(latlng);
	    }
	    else {
	    	var address = "<%= item.getLocation().getLocation() %>" + ", " + "<%= item.getCountry() %>";
	    	var geocoder = new google.maps.Geocoder();
	    	geocoder.geocode({"address" : address}, function (results, status) {
	    		if(status == google.maps.GeocoderStatus.OK) {
	    			var latlng = results[0].geometry.location;
	    			draw(latlng);
	    		}
	    	});
	    }
	  }
	</script> 
</head>
<body onload = "initialize()"> 
<form action="item" method="GET">
<p> Please type in the item id you want to search! </p>
	<input type="text" name="id">
	<br>
	<br>
	<input type="submit" value="Search">
</form>
<h3> The basic infromation of the item <%= request.getAttribute("query") %> </h3>
<table border="1" width="80%">
	<tr>
		<th width="20%"></th>
		<th width="80%"></th>
	</tr>
	<tr>
		<td>ItemID</td>
		<td><%= item.getItemId() %></td>
	</tr>
	<tr>
		<td>Name</td>
		<td><%= item.getName() %></td>
	</tr>
	<tr>
		<td>Category</td>
		<td><%= item.getCategories() %></td>
	</tr>
	<tr>
		<td>Currently</td>
		<td><%= item.getCurrently() %></td>
	</tr>
	<tr>
		<td>Buy Priece</td>
		<% if(item.getBuyPrice() != null) {%>
		<td><%= item.getBuyPrice() %></td>
		<% }else{ %>
		<td></td>
		<% } %>
	</tr>
	<tr>
		<td>First Bid</td>
		<td><%= item.getFirstBid() %></td>
	</tr>
	<tr>
		<td>Number of Bids:</td>
		<td><%= item.getNumberOfBids() %></td>
	</tr>
	<tr>
		<td>Location</td>
		<td>
			<ul>
				<li><%= item.getLocation().getLocation() %></li>
				<% if(item.getLocation().getLatitude() != null) { %>
				<li>Latitude: <%= item.getLocation().getLatitude() %></li>
				<% }else{ %>
				<li>Lattitude: </li>
				<% } %>
				<% if(item.getLocation().getLongitude() != null) { %>
				<li>Longitude: <%= item.getLocation().getLongitude() %></li>
				<% }else{ %>
				<li>Longitude: </li>
				<% } %>
			</ul>
		</td>
	</tr>
	<tr>
		<td>Country</td>
		<td><%= item.getCountry() %></td>
	</tr>
	<tr>
		<td>Started</td>
		<td><%= item.getStarted() %></td>
	</tr>
	<tr>
		<td>Ends</td>
		<td><%= item.getEnds() %></td>
	</tr>
	<tr>
		<td>Seller</td>
		<td>
			<ul>
				<li>Name: <%= item.getSeller().getUserID() %></li>
				<li>Rating: <%= item.getSeller().getRating() %></li>
			</ul>
		</td>
	</tr>
	<tr>
		<td>Description</td>
		<td><%= item.getDescription() %></td>
	</tr>
</table>
<h3> The bids infromation of the item <%= request.getAttribute("query") %> </h3>
<table border = "1" width = "80%">
	<tr>
		<th>Bidder Id</th>
		<th>Bidder Rating</th>
		<th>Bidder Loation</th>
		<th>Bidder Country</th>
		<th>Bid Time</th>
		<th>Bid Amount</th>
	</tr>
	<% 	List<Bid> bids = item.getBids();
		for (int i = 0; i < bids.size(); i++) { %>
		<tr>
			<td><%= bids.get(i).getBidder().getBidderId() %></td>
			<td><%= bids.get(i).getBidder().getBidderRating() %></td>
			<td><%= bids.get(i).getBidder().getLocation() %></td>
			<td><%= bids.get(i).getBidder().getCountry() %></td>
			<td><%= bids.get(i).getTime() %></td>
			<td><%= bids.get(i).getAmount() %></td>
		</tr>
	<% } %>
</table>
<div id="map_canvas" style="width:80%; height:80%"></div> 
</body>
</html>
