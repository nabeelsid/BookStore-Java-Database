import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QueryFunctions {
	/**
	 * Updates name and city of a given customer with customer ID
	 * @param customerName The new name to be updated with
	 * @param customerCity The new city to be updated with
	 * @param customerID The ID of the customer whose given information needs to be updated
	 * @param conDB Connection to the database
	 */
	public void updateCustomerInfo(String customerName, String customerCity, int customerID, Connection conDB) {
		String queryText = ""; // The SQL text.
		PreparedStatement querySt = null; // The query handle.
		int rowUpdateCount = 0; // initialize rowUpdateCount

		queryText = "Update yrb_customer set name = ?, city = ? where cid = ?";

		// Prepare the query
		try {
			querySt = conDB.prepareStatement(queryText);
		} catch (SQLException e) {
			System.out.println("SQL#1 failed in prepare");
			System.out.println(e.toString());
			System.exit(0);
		}
		// Execute the query.
		try {
			querySt.setString(1, customerName);
			querySt.setString(2, customerCity);
			querySt.setInt(3, customerID);
			rowUpdateCount = querySt.executeUpdate();
			// executeUpdate returns an int showing how many rows were affected by the update
			System.out.println(rowUpdateCount + " Row(s) were updated"); 
																		
		} catch (SQLException e) {
			System.out.println("SQL#1 failed in execute");
			System.out.println(e.toString());
			System.exit(0);
		}
		// We're done with the handle.
		try {
			querySt.close();
		} catch (SQLException e) {
			System.out.print("SQL#1 failed closing the handle.\n");
			System.out.println(e.toString());
			System.exit(0);
		}
	}

	/**
	 * Checks if the customer exists or not
	 * @param cid The ID of the customer to be checked in the database if they exist or not
	 * @param conDB Connection to the database
	 * @return Returns true if the customer with the given ID was found. Returns false otherwise
	 */
	public boolean customerCheck(int cid, Connection conDB) {
		String            queryText = "";     // The SQL text.
		PreparedStatement querySt   = null;   // The query handle.
		ResultSet         answers   = null;   // A cursor.

		boolean           inDB      = false;  // Return.

		queryText = "SELECT name " + "FROM yrb_customer " + "WHERE cid = ?" ;

		// Prepare the query.
		try {
			querySt = conDB.prepareStatement(queryText);
		} catch(SQLException e) {
			System.out.println("SQL#1 failed in prepare");
			System.out.println(e.toString());
			System.exit(0);
		}

		// Execute the query.
		try {
			querySt.setInt(1, cid);
			answers = querySt.executeQuery();
		} catch(SQLException e) {
			System.out.println("SQL#1 failed in execute");
			System.out.println(e.toString());
			System.exit(0);
		}

		// Any answer?
		try {
			if (answers.next()) {
				inDB = true;
				//custName = answers.getString("name");
			} else {
				inDB = false;
				//custName = null;
			}
		} catch(SQLException e) {
			System.out.println("SQL#1 failed in cursor.");
			System.out.println(e.toString());
			System.exit(0);
		} 
		// Close the cursor.
		try {
			answers.close();
		} catch(SQLException e) {
			System.out.print("SQL#1 failed closing cursor.\n");
			System.out.println(e.toString());
			System.exit(0);
		}

		// We're done with the handle.
		try {
			querySt.close();
		} catch(SQLException e) {
			System.out.print("SQL#1 failed closing the handle.\n");
			System.out.println(e.toString());
			System.exit(0);
		}
		return inDB;
	}
	
		/**
		 * Returns a string representation of customer's information
		 * @param cid The ID of the customer whose string representation if to be fetched
		 * @param conDB Connection to the database
		 * @return String representation of the customer with the given ID
		 */
		public String getCustomerInfo(int cid, Connection conDB){
			String            queryText = "";     // The SQL text.
			PreparedStatement querySt   = null;   // The query handle.
			ResultSet         answers   = null;   // A cursor.
			String customerInfo = "";
			queryText = "Select * from yrb_customer where cid = ?";

			try {
				querySt = conDB.prepareStatement(queryText);
			} catch(SQLException e) {
				System.out.println("SQL#1 failed in prepare");
				System.out.println(e.toString());
				System.exit(0);
			}
			// Execute the query.
			try {
				querySt.setInt(1, cid);
				answers = querySt.executeQuery();
			} catch(SQLException e) {
				System.out.println("SQL#1 failed in execute");
				System.out.println(e.toString());
				System.exit(0);
			}
			// Any answer?\
			try {
				if(answers.next()){
					customerInfo = answers.getInt("cid") + " " + answers.getString("name") +" "+ answers.getString("city");
				}
			} catch(SQLException e) {
				System.out.println("SQL#1 failed in cursor.");
				System.out.println(e.toString());
				System.exit(0);
			}
			// Close the cursor.
			try {
				answers.close();
			} catch(SQLException e) {
				System.out.print("SQL#1 failed closing cursor.\n");
				System.out.println(e.toString());
				System.exit(0);
			}

			// We're done with the handle.
			try {
				querySt.close();
			} catch(SQLException e) {
				System.out.print("SQL#1 failed closing the handle.\n");
				System.out.println(e.toString());
				System.exit(0);
			}
			return customerInfo;
		}
		
		/**
		 * Return all the categories of books available out all the categories offered by YRB
		 * @param conDB Connection to the database
		 * @return Returns an array of Strings containing all the categories of books available in the bookstore database
		 */
		public String[] showCategories(Connection conDB){
			String[] catArray = new String[12];
			int counter = 0;
			String            queryText = "";     // The SQL text.
			PreparedStatement querySt   = null;   // The query handle.
			ResultSet         answers   = null;   // A cursor.
			queryText = "Select * from yrb_category";

			try {
				querySt = conDB.prepareStatement(queryText);
			} catch(SQLException e) {
				System.out.println("SQL#1 failed in prepare");
				System.out.println(e.toString());
				System.exit(0);
			}
			// Execute the query.
			try {
				answers = querySt.executeQuery();
			} catch(SQLException e) {
				System.out.println("SQL#1 failed in execute");
				System.out.println(e.toString());
				System.exit(0);
			}
			// Any answer?\
			try {
				while(answers.next()){
					catArray[counter] = answers.getString("cat");
					counter++;   
				}
			} catch(SQLException e) {
				System.out.println("SQL#1 failed in cursor.");
				System.out.println(e.toString());
				System.exit(0);
			}
			// Close the cursor.
			try {
				answers.close();
			} catch(SQLException e) {
				System.out.print("SQL#1 failed closing cursor.\n");
				System.out.println(e.toString());
				System.exit(0);
			}

			// We're done with the handle.
			try {
				querySt.close();
			} catch(SQLException e) {
				System.out.print("SQL#1 failed closing the handle.\n");
				System.out.println(e.toString());
				System.exit(0);
			}
			return catArray;
		}
		
		/**
		 * Find a given title of the book in a specific category from the database
		 * @param cid The cid of the customer requesting the book. Used for returning the correct price of the book as the customer may belong to a variety of clubs
		 * @param title The title of the book that needs to be searched for
		 * @param cat The category of book that needs to be searched for
		 * @param conDB Connection to the database
		 * @return Returns an ArrayList of strings (book titles) containing all the books with the given title. Includes all the books with given keywords as title
		 */
		public ArrayList<String> findBookByCategory(int cid, String title, String cat, Connection conDB) {
			int counter                 = 0;
			ArrayList<String> bookList = new ArrayList<String>();
			String            queryText = "";     // The SQL text.
			PreparedStatement querySt   = null;   // The query handle.
			ResultSet         answers   = null;   // A cursor.
			queryText = "select distinct title, q3.bookYear, language, cat, weight from (select q1.title, q1.year as bookYear, q1.language, q1.cat, q1.year, q1.weight, q2.club from (select * from yrb_book) as q1 inner join (select * from yrb_offer) as q2 on q1.title = q2.title and q1.year = q2.year) as q3 inner join (select * from yrb_member) as q4 on q3.club = q4.club where cid = ? and cat = ? and title = ?";
			//prepare query
			try {
				querySt = conDB.prepareStatement(queryText);
			} catch(SQLException e) {
				System.out.println("SQL#1 failed in prepare");
				System.out.println(e.toString());
				System.exit(0);
			}

			// Execute the query.
			try {
				querySt.setInt(1, cid);
				querySt.setString(2, cat);
				querySt.setString(3, title);
				answers = querySt.executeQuery();
			} catch(SQLException e) {
				System.out.println("SQL#1 failed in execute");
				System.out.println(e.toString());
				System.exit(0);
			}

			// Any answer?\
			try {
				while(answers.next()){
					String answer = (answers.getString(1) +" "+ answers.getInt(2) +" "+ answers.getString(3) +" "+ answers.getString(4) +" "+ answers.getInt(5));
					bookList.add(counter, answer);
					counter++;   
				}

			} catch(SQLException e) {
				System.out.println("SQL#1 failed in cursor.");
				System.out.println(e.toString());
				System.exit(0);
			}

			// Close the cursor.
			try {
				answers.close();
			} catch(SQLException e) {
				System.out.print("SQL#1 failed closing cursor.\n");
				System.out.println(e.toString());
				System.exit(0);
			}

			// We're done with the handle.
			try {
				querySt.close();
			} catch(SQLException e) {
				System.out.print("SQL#1 failed closing the handle.\n");
				System.out.println(e.toString());
				System.exit(0);
			}
			return bookList;
		}
		
		/**
		 * Returns the minimum price of the given book
		 * @param cid The ID of the customer requesting the book price
		 * @param title The title of the book the customer is trying to get price of
		 * @param cat The category in which the customer wants to search in
		 * @param conDB Connection to the database
		 * @return Returns the minimum price of the book
		 */
		public double minimumPrice(int cid, String title, String cat, Connection conDB){
			double               price = 0;
			String            queryText = "";     // The SQL text.
			PreparedStatement querySt   = null;   // The query handle.
			ResultSet         answers   = null;   // A cursor.
			queryText = "select q3.price, q3.club from (select q1.title, q1.year, q1.language, q1.cat, q1.year, q2.price, q2.club from (select * from yrb_book) as q1 inner join (select * from yrb_offer) as q2 on q1.title = q2.title and q1.year = q2.year) as q3 inner join (select * from yrb_member) as q4 on q3.club = q4.club where cid = ? and cat = ? and title = ? and q3.price = (select min(q3.price) from (select q1.title, q1.year, q1.language, q1.cat, q1.year, q2.price, q2.club from (select * from yrb_book) as q1 inner join (select * from yrb_offer) as q2 on q1.title = q2.title and q1.year = q2.year) as q3 inner join (select * from yrb_member) as q4 on q3.club = q4.club where cid = ? and cat = ? and title = ?)";

			//prepare query
			try {
				querySt = conDB.prepareStatement(queryText);
			} catch(SQLException e) {
				System.out.println("SQL#1 failed in prepare");
				System.out.println(e.toString());
				System.exit(0);
			}

			// Execute the query.
			try {
				querySt.setInt(1, cid);
				querySt.setString(2, cat);
				querySt.setString(3, title);
				querySt.setInt(4, cid);
				querySt.setString(5, cat);
				querySt.setString(6, title);
				answers = querySt.executeQuery();
			} catch(SQLException e) {
				System.out.println("SQL#1 failed in execute");
				System.out.println(e.toString());
				System.exit(0);
			}

			// Any answer?\
			try {
				if(answers.next()){
					price = answers.getDouble(1);   
				}

			} catch(SQLException e) {
				System.out.println("SQL#1 failed in cursor.");
				System.out.println(e.toString());
				System.exit(0);
			}

			// Close the cursor.
			try {
				answers.close();
			} catch(SQLException e) {
				System.out.print("SQL#1 failed closing cursor.\n");
				System.out.println(e.toString());
				System.exit(0);
			}

			// We're done with the handle.
			try {
				querySt.close();
			} catch(SQLException e) {
				System.out.print("SQL#1 failed closing the handle.\n");
				System.out.println(e.toString());
				System.exit(0);
			}
			return price;
		}
		
		/**
		 * Returns the minimum price of the book from all the clubs a customer is member of
		 * @param cid The ID of the customer requesting the minimum price of the book 
		 * @param title The title of the book to be searched for
		 * @param cat The category of the book in which to search the book
		 * @param conDB Connection to the database
		 * @return Returns the minimum price of the book from all the clubs customer is member of
		 */
		public String minimumPriceClub(int cid, String title, String cat, Connection conDB){
			String            club      = "";
			String            queryText = "";     // The SQL text.
			PreparedStatement querySt   = null;   // The query handle.
			ResultSet         answers   = null;   // A cursor.
			queryText = "select q3.price, q3.club from (select q1.title, q1.year, q1.language, q1.cat, q1.year, q2.price, q2.club from (select * from yrb_book) as q1 inner join (select * from yrb_offer) as q2 on q1.title = q2.title and q1.year = q2.year) as q3 inner join (select * from yrb_member) as q4 on q3.club = q4.club where cid = ? and cat = ? and title = ? and q3.price = (select min(q3.price) from (select q1.title, q1.year, q1.language, q1.cat, q1.year, q2.price, q2.club from (select * from yrb_book) as q1 inner join (select * from yrb_offer) as q2 on q1.title = q2.title and q1.year = q2.year) as q3 inner join (select * from yrb_member) as q4 on q3.club = q4.club where cid = ? and cat = ? and title = ?)";

			//prepare query
			try {
				querySt = conDB.prepareStatement(queryText);
			} catch(SQLException e) {
				System.out.println("SQL#1 failed in prepare");
				System.out.println(e.toString());
				System.exit(0);
			}

			// Execute the query.
			try {
				querySt.setInt(1, cid);
				querySt.setString(2, cat);
				querySt.setString(3, title);
				querySt.setInt(4, cid);
				querySt.setString(5, cat);
				querySt.setString(6, title);
				answers = querySt.executeQuery();
			} catch(SQLException e) {
				System.out.println("SQL#1 failed in execute");
				System.out.println(e.toString());
				System.exit(0);
			}

			// Any answer?\
			try {
				if(answers.next()){
					club = answers.getString(2);   
				}

			} catch(SQLException e) {
				System.out.println("SQL#1 failed in cursor.");
				System.out.println(e.toString());
				System.exit(0);
			}

			// Close the cursor.
			try {
				answers.close();
			} catch(SQLException e) {
				System.out.print("SQL#1 failed closing cursor.\n");
				System.out.println(e.toString());
				System.exit(0);
			}

			// We're done with the handle.
			try {
				querySt.close();
			} catch(SQLException e) {
				System.out.print("SQL#1 failed closing the handle.\n");
				System.out.println(e.toString());
				System.exit(0);
			}
			return club;
		}
		
		/**
		 * Execute a purchase order and add the transaction to the database
		 * @param custID The ID of the customer who is trying to complete their order
		 * @param title The title of the book to be purchased
		 * @param club The club of the given customer
		 * @param bookYear The year of the book
		 * @param when The time of the purchase
		 * @param quantity The quantity of books purchasing
		 * @param conDB Connection to database
		 * @return Returns true if the purchase was successful and the database was updated successfully with given information
		 */
		public boolean makePurchase(int custID, String title, String club, int bookYear, String when, int quantity, Connection conDB){
			String            queryText = "";     // The SQL text.
			PreparedStatement querySt   = null;   // The query handle.
			boolean result   = false;             //initialize rowUpdateCount

			queryText = "insert into yrb_purchase (cid,club,title,year,when,qnty) values (?, ?, ?, ?, ?, ?)";

			// Prepare the query.
			try {
				querySt = conDB.prepareStatement(queryText);
			} catch(SQLException e) {
				System.out.println("SQL#1 failed in prepare");
				System.out.println(e.toString());
				System.exit(0);
			}
			// Execute the query.
			try {
				querySt.setInt(1, custID);
				querySt.setString(2, club);
				querySt.setString(3, title);
				querySt.setInt(4, bookYear);
				querySt.setString(5, when);
				querySt.setInt(6, quantity);
				querySt.executeUpdate();
				result  = true;

			} catch(SQLException e) {
				System.out.println("SQL#1 failed in execute");
				System.out.println(e.toString());
				System.exit(0);
			}
			// We're done with the handle.
			try {
				querySt.close();
			} catch(SQLException e) {
				System.out.print("SQL#1 failed closing the handle.\n");
				System.out.println(e.toString());
				System.exit(0);
			}
			return result;
		}
}
