Yang Pei 304434922
Single 

In the keyword search part, the default numResultsToSkip is set to 0 and the default numResultsToReturn is set to 20. When the user change the parameter in the url and the parameter is invalid, I would set the parameter correctly to the default value. When the user set numResultsToReturn to 0, the value is set to some big enough number to return all the result. If the user input nothing in the textbox, then the result would show noting.

In the item search part, if the item id does not exist, the user would be redirect to the error page informing them that the item does not exist. The bid infromation of the item is sorted according the the time in descednding order. If the item's location could not be obtained, the google map would not show up.

In the google suggestion part, the user input is encoded using UTF-8, and when there is no input in the textbox, there would be no suggestions.

I used a css file available on the web to make the website more user friendly.