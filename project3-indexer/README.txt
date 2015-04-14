Yang Pei
304434922

During the build of the Lucene index, I use an object Item to hold the necessary information in memory and using a hash map to hold them. This is because I need to do a join operation to get all of the category information. To build the spatial index, I put build a new table with all the item that has latitude and longitude. 