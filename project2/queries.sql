# Find the number of users in the database
SELECT COUNT(*) FROM AcutionUser;

#Find the number of items in "New York"
SELECT COUNT(*) FROM Item WHERE BINARY Location = 'New York';

#Find the number of auctions belonging to exactly four categories
SELECT COUNT(*) FROM (SELECT * FROM BelongsTo GROUP BY Iid HAVING COUNT(*) = 4) AS T; 

#Find the ID(s) of current (unsold) auction(s) with the highest bid
SELECT ItemID FROM Item WHERE Currently = (
	SELECT MAX(Currently) FROM Item WHERE Ends > '2001-12-20 00:00:01' and Number_of_Bids > 0)
	and Number_of_Bids > 0 and Ends > '2001-12-20 00:00:01';

#Find the number of sellers whose rating is higher than 1000
SELECT COUNT(*) FROM AcutionUser WHERE SellRating > 1000;

#Find the number of users who are both sellers and bidders
SELECT COUNT(*) FROM AcutionUser WHERE SellRating IS NOT NULL AND BidRating IS NOT NULL;

#Find the number of categories that include at least one item with a bid of more than $100
SELECT COUNT(*) FROM (
	SELECT DISTINCT Cid FROM Item, BelongsTo, BidsInfo WHERE 
	Item.ItemID = BelongsTo.Iid and Item.ItemID = BidsInfo.Iid and Amount > 100) AS T;
