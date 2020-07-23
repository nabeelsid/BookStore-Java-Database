import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

public class jdbc {
	private static final Pattern p = Pattern.compile("[0-9]{4}"); // to extract the year from the books result
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss"); // date format for purchase

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		Connection conDB; //// URL: Which database?
		String url;
		Integer custID;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		QueryFunctions qf = new QueryFunctions();
		try {
			// Register the driver with DriverManager.
			Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
		} catch (ClassNotFoundException e) {
			System.out.println("Could not find class");
			e.printStackTrace();
			return;
		} catch (InstantiationException e) {
			System.out.println("Could not instantiate driver instance");
		} catch (IllegalAccessException e) {
			System.out.println(
					"Currently executing method does not have access to the definition of the specified class, field, method or constructor");
		}

		url = "jdbc:db2:c3421a"; // URL: This database.
		try {
			conDB = DriverManager.getConnection(url);
			if (conDB != null) {
				System.out.println("DB2 Database Connected");
			} else {
				System.out.println("Db2 connection Failed ");
			}
		} catch (SQLException e) {
			System.out.println("DB2 Database connection Failed");
			e.printStackTrace();
			return;
		}
		// -------------------------CLI Start-----------------------------
		System.out.print("\033[H\033[2J");
		System.out.flush();

		Scanner input = new Scanner(System.in);

		System.out.println("************* YRB Online Bookstore *************");
		System.out.println("Please enter a customer number:"); // Promt the user
		System.out.print("Customer#:");

		custID = input.nextInt(); // take input as int

		if (!qf.customerCheck(custID, conDB)) {
			do {
				System.out.println("Error: Not a valid customer ID. Please try again or enter 0 to exit");
				System.out.print("Customer#:");
				while (!input.hasNextInt()) {
					System.out.println("That's not a number!");
					input.next(); // this is important!
					System.out.print("Customer#:");
				}

				custID = input.nextInt();
				if (custID == 0) {
					System.exit(0);
				}
			} while (!qf.customerCheck(custID, conDB)); // while customer does not exist, keep looping
		}
		System.out.println(qf.getCustomerInfo(custID, conDB));
		System.out.print("Would you like to update the customer information? (Y/N):");
		String choice = input.next();

		if (choice.equals("Y") || choice.equals("y")) {
			input.nextLine();

			String customerName = "";
			String customerCity = "";

			System.out.print("Customer Name: ");
			customerName = input.nextLine();

			System.out.print("Customer City: ");
			customerCity = input.nextLine();
			qf.updateCustomerInfo(customerName, customerCity, custID, conDB);
		}

		boolean trigger = true;

		while (trigger) {
			System.out.print("\033[H\033[2J");
			System.out.flush();
			System.out.println("************* Book Categories **************");
			
			//Store all the categories in an array by calling showCategories
			String[] category = qf.showCategories(conDB);
			
			//Print all the category returned from the category array
			for (int i = 0; i < category.length; i++) {
				System.out.println("  " + (i + 1) + ". " + category[i]);
			}
			
			System.out.print("Choose a category# or enter 0 to exit: ");
			int categoryInt; // category select and exit condition int
			String title = "";
			categoryInt = input.nextInt();
			if (categoryInt == 0) {
				System.out.println("Good bye!");
				System.exit(0);
			}
			
			System.out.println("Category " + category[categoryInt - 1] + " is selected.");
			input.nextLine(); // flush out the scanner to prepare for the next input.
			System.out.print("Would you like to continue? (Y/N):");
			choice = input.nextLine();
			
			if (choice.equals("N") || choice.equals("n")) {
				System.out.println("Good bye!");
				System.exit(0);
			}

			System.out.print("Please enter the title of the book to begin search:");
			title = input.nextLine();
			
			ArrayList<String> books = qf.findBookByCategory(custID, title, category[categoryInt - 1], conDB);
			
			if (books.size() == 0) {
				System.out.println("\nThe book is not available");
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				trigger = false; // if the book is available then the next step is to show the books available
									// and stop the while loop
				System.out.println(
						"TITLE                    YEAR  LANGUAGE CAT WEIGHT\n------------------------ ---- -------- --- -----");
				for (int i = 0; i < books.size(); i++) {
					System.out.println((i + 1) + ". " + books.get(i));
				}
				int bookInt; // book select
				System.out.print("Select a book# to purchase:");
				bookInt = input.nextInt();
				
				// Display minimum price
				// Decode the answers string to get specific columns
				double minimumPurchasePrice = qf.minimumPrice(custID, title, category[categoryInt - 1], conDB);
				System.out.println("The minimum purchase price of the book is: " + minimumPurchasePrice);
				
				// Ask user to enter the quantity
				int quantity;
				System.out.print("Enter the quantity of books to be purchased:");
				quantity = input.nextInt();
				
				// display the total price
				System.out.printf("Total price of purchase is: %.2f\n", (minimumPurchasePrice * quantity));
				input.nextLine(); // flush scanner again to take next input
				
				//Ask confirmation to customer
				System.out.print("Would you like to purchase the book/books? (Y/N):");
				choice = input.nextLine();
				
				if (choice.equals("N") || choice.equals("n")) {
					System.out.print("Would you like to continue?:");
					choice = input.nextLine();
					if (choice.equals("N") || choice.equals("n")) {
						System.out.println("Good bye!");
						System.exit(0);
					}
					trigger = true;
				} else {
					// make the purchase
					String club = qf.minimumPriceClub(custID, title, category[categoryInt - 1], conDB);
					
					// to extract the year used in insert of book purchase
					Matcher m = p.matcher(books.get(bookInt - 1)); 
					int bookYear = 0;
					if (m.find()) {
						bookYear = Integer.parseInt(m.group(0));
					}
					String when = (sdf.format(timestamp) + "");
					boolean result = qf.makePurchase(custID, title, club, bookYear, when, quantity, conDB);
					if (!result) {
						System.out.println("Purchase Unsuccessful!");
						try {
							TimeUnit.SECONDS.sleep(3);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						trigger = true;
					} else {
						System.out.println("Thank you for your purchase.");
					}
				}
			}
		}
		input.close();
	}// main method
}// class