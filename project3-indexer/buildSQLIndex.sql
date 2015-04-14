CREATE TABLE Geom (
	ItemID varchar(300) primary key,
	Location Point not null
) ENGINE = MyISAM;

CREATE SPATIAL INDEX sp_index ON Geom(Location);

INSERT INTO Geom (ItemID, Location) 
	SELECT ItemID, Point(0 + Latitude, 0 + Longitude)
	FROM Item
	WHERE Latitude is not null and Longitude is not null


