package il.ac.colman.cs;

import java.sql.SQLException;

import il.ac.colman.cs.util.DataStorage;
import il.ac.colman.cs.util.LinkExtractor;

public class LinkListener {
  public static void main(String[] args) throws SQLException {
    // Connect to the database
    DataStorage dataStorage = new DataStorage();

    // Initiate our link extractor
    LinkExtractor linkExtractor = new LinkExtractor();

    // Listen to SQS for arriving links
    // ...

    // Extract the link content
    // ...

    // Take screenshot
    // ...

    // Save everything in the database
  }
}
