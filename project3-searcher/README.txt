Yang Pei
304434922

The basic search part is easy to implement. In order to combine lucene index and spatial index, I first query using both index independently and then make a intersection of the result set. To return the XML document of an item, I choose to use the DOM to build the XML document and then doing some necessary format to return it as a string.