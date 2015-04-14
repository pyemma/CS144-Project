CREATE TABLE AcutionUser(
	UserID varchar(300) primary key,
	Location varchar(300),
	Country varchar(300),
	SellRating varchar(300),
	BidRating varchar(300)
) ENGINE = INNODB;

CREATE TABLE Item(
	ItemID varchar(300) primary key,
	Name varchar(300) not null,
	Currently decimal(8, 2) not null,
	Buy_Price decimal(8, 2),
	First_Bid decimal(8, 2) not null,
	Number_of_Bids int,
	Location varchar(300),
	Latitude varchar(300),
	Longitude varchar(300),
	Country varchar(300),
	Started Timestamp,
	Ends Timestamp,
	Seller varchar(300),
	Description varchar(4000),
	FOREIGN KEY (Seller) REFERENCES AcutionUser(UserID)
) ENGINE = INNODB;

CREATE TABLE BidsInfo(
	Iid varchar(300),
	Uid varchar(300),
	Time Timestamp,
	Amount decimal(8, 2),
	PRIMARY KEY (Iid, Uid, Time),
	FOREIGN KEY (Iid) REFERENCES Item(ItemID),
	FOREIGN KEY (Uid) REFERENCES AcutionUser(UserID)
) ENGINE = INNODB;

CREATE TABLE Category(
	CateId int primary key,
	Type varchar(300)
) ENGINE = INNODB;

CREATE TABLE BelongsTo(
	Iid varchar(300),
	Cid int,
	PRIMARY KEY(Iid, Cid),
	FOREIGN KEY (Iid) REFERENCES Item(ItemID),
	FOREIGN KEY (Cid) REFERENCES Category(CateID)
) ENGINE = INNODB;