Yang Pei 304434922

My schema design is as follow:
AcutionUser(UserID, Location, Country, SellRating, BidRating), here, the UserID is the primary key.

Item(ItemID, Name, Currently, Buy_Price, First_Bid, Number_of_Bids, Location, Latitude, Longitude, Country, Started, Ends, Seller, Description), here the ItemID is the primary key and Seller is a foreign key referencing to UserID in AcutionUser.

BidsInfo(Iid, Uid, Time, Amount),
here (Iid, Uid, Time) together form the primary key, and Iid is a foreign key referencing ItemID in Item, Uid is a foreign key referencing UserID in AcutionUser.

Category(CateId, Type),
here CateId is the primary key.

BelongsTo(Iid, Cid),
here (Iid, Cid) together form the primary key, and Iid is a foreign key referencing ItemID in Item, Cid is a foreign key referecing CateId in
Category.

The relations are
(ItemId, Name, Category, Currently, Buy_Price, First_Bid, Number_of_Bids, Location, Latitude, Longitude, Country, Started, Ends, SellerID, SellRating, Description, Time, Amount, BidderID, BidRating)

The functional dependencies are:
BidderID -> Location, Country, SellRating, BidRating
ItemID -> Name, Currently, Buy_Price, First_Bid, Number_of_Bids, Location, Latitude, Longitiude, Country, Started, Ends, Seller, Description
Iid, Uid, Time -> Amount

Since Bidder and Seller could be the same person, I use UserID to combine them together. According to these functional dependencies, we could use BCNF to decomposit the relation.

There is also a multi dependence ItemID ->> Category, so we depcompose the remaining relation into two tables BidInfo and BelongsTo.

Here in the design, I assume that the location of an Item has noting to do with the location of the seller.