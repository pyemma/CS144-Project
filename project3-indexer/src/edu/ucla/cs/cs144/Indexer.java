package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;
import java.util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;



public class Indexer {

    /** Class to represent an Item record */
    private static class Item {
        String itemId, name, description;
        List<String> categories;
        public Item() {
            itemId = "";
            name = "";
            description = "";
            categories = new ArrayList<String>();
        }

        public String getFullText() {
            StringBuffer sb = new StringBuffer();
            sb.append(name); sb.append(" ");
            for(int i = 0; i < categories.size(); i++) {
                sb.append(categories.get(i));
                sb.append(" ");
            }
            sb.append(description);
            return sb.toString();
        }
    }
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }

    private IndexWriter indexWriter = null;

    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if(indexWriter == null) {
            String filepath = "/var/lib/lucene/";
            Directory indexDir = FSDirectory.open(new File(filepath));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
    }

    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }

    /* Add the Item document into the index */
    public void indexItem(Item item) throws IOException {

        IndexWriter writer = getIndexWriter(false);
        Document doc = new Document();
        doc.add(new StringField("id", item.itemId, Field.Store.YES));
        doc.add(new StringField("name", item.name, Field.Store.YES));    
        doc.add(new TextField("content", item.getFullText(), Field.Store.NO));
        writer.addDocument(doc);
    }
 
    public void rebuildIndexes() {

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
	} catch (SQLException ex) {
	    System.out.println(ex);
	}


	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */

    HashMap<String, Item> map = new HashMap<String, Item>();
    String itemId, name, description, category;
    /* Retrieve the data from database and store it in Item object*/
    try {

        Statement s = conn.createStatement();

        String query = "SELECT ItemID, Name, Type, Description FROM Item, BelongsTo, Category WHERE ItemId = Iid and Cid = CateId";

        ResultSet rs = s.executeQuery(query);

        while(rs.next()) {
            itemId = rs.getString("ItemID");
            name = rs.getString("Name");
            description = rs.getString("Description");
            category = rs.getString("Type");

            if(map.containsKey(itemId) == true) {
                map.get(itemId).categories.add(category);
            }
            else {
                Item item = new Item();
                item.itemId = itemId;
                item.name = name;
                item.description = description;
                item.categories.add(category);
                map.put(itemId, item);
            }
        }

        /* close the resultset, statement */
        rs.close();
        s.close();

        /* Insert each Item into the index */

        getIndexWriter(true);

        for (String str : map.keySet()) {
            indexItem(map.get(str));
        }

        closeIndexWriter();

    } catch (SQLException ex) {
        System.out.println("SQLException caught");
        System.out.println("---");
        while ( ex != null ){
            System.out.println("Message   : " + ex.getMessage());
            System.out.println("SQLState  : " + ex.getSQLState());
            System.out.println("ErrorCode : " + ex.getErrorCode());
            System.out.println("---");
            ex = ex.getNextException();
        }
    } catch (IOException ex) {
        System.out.println(ex);
    }


        // close the database connection
	try {
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
    }



    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
