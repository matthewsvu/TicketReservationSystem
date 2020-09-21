/*
 * Name: Matthew Vu
 * Class: CS 2336
 * ID: MSV180000
 * Purpose: Ticket reservation system for a movie theater. Uses file i/o to make
 * reservations and outputs results to output file.
 */
import java.io.*; // for input/output
import java.util.*; // for scanner object
import java.text.DecimalFormat;

public class Main {
	// TICKET COSTS
	public static final double ADULT_TICKET_COST = 10;
    public static final double CHILD_TICKET_COST = 5;
	public static final double SENIOR_TICKET_COST = 7.5;
	// FOR AUDITORIUM HEADER START OF ASCII CHARACTER ALPHABET
	public static final int ASCII_CHAR_START = 65;  
	/// ROWS AND COLS WILL NEVER EXCEED THESE
	public static final int MAX_ROWS = 10;
	public static final int MAX_COLUMNS = 26;
	
	// used for calculations for each auditorium
	static int totalCols = 0;
	static int totalRows = 0;
	
	// inits 2d array for auditorium 10 rows, 26 columns
	static String[][] auditorium = new String[MAX_ROWS][MAX_COLUMNS];
	
	// used to round decimals to hundredth place
	private static DecimalFormat df = new DecimalFormat("0.00");
		
	// replaces system.out.println for convenience
	public static void printLine(String str) {
		System.out.println(str);
	}
	
	// calculated distance between middle and optimal seating
	public static int calculateDistance(int seatsRow, int totalQuantity, int outerLoopCount) {
		int distance = (outerLoopCount + (totalQuantity) / 2) - ((seatsRow+1) / 2);
		return distance;
	}
	
	// checks for the best available seats to reserve
	public static int bestAvailable(String[][] audit, int seatsRow, int totalQuantity, int rowNum) {
		int startSeatNum = -1; // default value that is needed returned when no best available seats are found
		double smallDist = Integer.MAX_VALUE, currDist = Integer.MAX_VALUE; // used to swap between each other when a smaller dist is found
		
		for(int i = 0; i < seatsRow-totalQuantity+1; i++) { // stops right when continuing would go out of bounds
			boolean empty = true;
			for(int j = 0; j < totalQuantity; j++) { // loop only the total quanitity number of times 
				if(!audit[rowNum][j+i].contains(".")) { // breaks from loop if current seat is occupied
					empty = false;
					break;
				}
			}
			// after exiting loop if all the seats were empty calculate the distance
			if(empty) {
				currDist = Math.abs(calculateDistance(seatsRow, totalQuantity, i));
			}
			// if the distance calculated is less than the current smallest distance replace it
			if(currDist < smallDist) {
				startSeatNum = i;  // set the seat number to the current index
				smallDist = Math.min(currDist, smallDist);
			}
		}
		return startSeatNum;
	}
	// helper method: generates the header for the display auditorium and the valid seats available
	public static void rowLetterGenerator() {
		System.out.print("  ");
		for(int i = 0; i < totalCols; i++) {
			char letter = (char)(i+ASCII_CHAR_START); // cast int to ascii char
			System.out.print(letter); 
		}
		printLine("");
	}
	// displays what the auditorium looks like to user
	public static void displayAuditorium(String[][] audit) {
		rowLetterGenerator();	
		for(int i = 0; i < totalRows; i++) {
			System.out.print(i+1 + " "); // prints number on the left hand side of the columns
			for(int j = 0; j < totalCols; j++) {
				// print # instead of the letters for occupied seats
				if(audit[i][j].equals("A") || audit[i][j].equals("S") || audit[i][j].equals("C")) { 
					System.out.print("#");
				}
				else {
					System.out.print(".");
				}
			}
			printLine("");
		}	
	} 	
	
	//allows user to reserve seats (child, senior, adult) checks if they are available
	public static void reserveSeats(int row, String seat, int adultQuantity, int seniorQuantity, int childQuantity) {
		int seatNum = (int)(seat.charAt(0)) - ASCII_CHAR_START; // set string to char and then cast to int. Subtract to zero it
		
		// goes through each ticket type and reserves in order of adults, children, and seniors
		for(int i = 0; i < adultQuantity; i++) {
			auditorium[row-1][seatNum] = "A";
			seatNum++;
		}
		for(int j = 0; j < childQuantity; j++) {
			auditorium[row-1][seatNum] = "C";
			seatNum++;
		}
		for(int k = 0; k < seniorQuantity; k++) {
			auditorium[row-1][seatNum] = "S";
			seatNum++;
		}
	}
	
	// helper function to reserve seats, checks if seat is available
	public static boolean checkAvailable(String[][] audit, int row, String seat, int totalQuantity) {
		int seatNum = (int) (seat.charAt(0)) - ASCII_CHAR_START;
	
		for(int i = 0; i < totalQuantity; i++) { // loop through the same amount seats the user reserved
			if(!audit[row-1][seatNum].contains(".")) { // if the seat is occupied return false
				return false;
			}
			seatNum++; // increment the seat number otherwise to check the next seat
		}
		return true;
	}
	
	// method outputs 2d array to output file A1.txt
	public static void outputArrayFile(PrintWriter output) throws IOException {
		for(int i = 0; i < totalRows; i++) {
			for(int j = 0; j < totalCols; j++) {
				output.print(auditorium[i][j]); // print it to A1.txt
			}
			output.println("");
		}
	}
	
	// displays final report of tickets, types, and seats sold to user
	public static void displayReport() {
		int totalSold = 0, adultSold = 0, childSold = 0, seniorSold = 0;
		double totalSales = 0;
		// Counts num sold by going through each seat and incrementing based on type found
		for(int i = 0; i < totalRows; i++) {
			for(int j = 0; j < totalCols; j++) { 
				switch(auditorium[i][j]) {
					case "A":
						adultSold++;
						break;
					case "C":
						childSold++;
						break;
					case "S":
						seniorSold++;
						break;
				}
			}
		}
		//Calculates and displays the num sold and sub types.
		totalSales = (adultSold * ADULT_TICKET_COST) + (childSold * CHILD_TICKET_COST) + (seniorSold * SENIOR_TICKET_COST);
		totalSold =  (seniorSold + childSold + adultSold);
		System.out.println("Total Seats:	" + (totalCols * totalRows) + "\n" +
						   "Total Tickets:	" + totalSold + "\n" +
						   "Adult Tickets:	" + adultSold + "\n" +
						   "Child Tickets:	" + childSold + "\n" +
						   "Senior Tickets:	" + seniorSold + "\n" +
						   "Total Sales:	$" + df.format(totalSales)); // rounded to 2 decimal places
	}
	
	// method opens file and reads it into 2d array, checks if it exists
	public static void openAuditFile (String filename) throws FileNotFoundException {
		try {
			File auditFile = new File(filename);
			Scanner fileOpen = new Scanner(auditFile);
			
			if(auditFile.canRead()) { // check if file is found
				// I want to read in a 2d array into auditorium.
				while(fileOpen.hasNext()) {
					String currRow = fileOpen.nextLine(); // creates a string of just one line
					for(int i = 0; i < currRow.length(); i++) {
						if(totalCols == 0) { // finds the amount of columns in each row
							totalCols = currRow.length();
						}
						String index = Character.toString(currRow.charAt(i)); // parses through the line and assigns it to auditorium
						auditorium[totalRows][i] = index;
					}
					totalRows++; // increment to move on to the next row and count # rows
				}
					fileOpen.close();
			}
		}
		catch (Exception e) { // checks if file exists
			printLine("Error: file not found");
		}
	}
	
	// main logic for asking user to reserve tickets and best available seats
	public static void userInterfaceLoop(Scanner input, PrintWriter output) throws IOException {
		boolean EXIT = false; // used to exit the do loop when user is prompted to exit
		
		do {
			int rowNum = 0, adultNum = 0, childNum = 0, seniorNum = 0; // stores the row number and the num of tickets for each type
			String seatLetter = ""; // used to reserve seats and check if it's available
		
			printLine("1. Reserve Seats");
			printLine("2. Exit");
			// scanner for user selection in menu
			String menuSelect = input.next();
			switch (menuSelect) {
				case "1": // when user chooses to reserve seats
					displayAuditorium(auditorium); 
					// input validation and prompts to gather information on what seats user wants
					printLine("Please enter a row number from above: ");
					rowNum = input.nextInt();
					while(rowNum <= 0 || rowNum > totalRows) {
						displayAuditorium(auditorium);
						printLine("Please try again and enter a valid row number: ");
						rowNum = input.nextInt();
					}
					// only allows capital letters and the ABCs as viable strings to enter
					printLine("Enter a valid seat letter: ");
					while(!input.hasNext("[ABCDEFGHIJKLMNOPQRSTUVWXYZ]")) {
						printLine("Invalid letter, please choose a valid seat letter");
						seatLetter = input.next().toUpperCase();
					}
					seatLetter = input.next().toUpperCase();
					// gathers ticket amount along with input validation for all sub types
					printLine("How many adult tickets?: ");
					adultNum = input.nextInt();
					while(adultNum < 0) {
						printLine("Invalid number of tickets, please enter a number between 0 and " + totalCols);
						adultNum = input.nextInt();
					}
					
					System.out.println("How many child tickets?: ");
					childNum = input.nextInt();
					while(childNum < 0) {
						printLine("Invalid number of tickets, please enter a number between 0 and " + totalCols);
						childNum = input.nextInt();
					}
					
					printLine("How many senior tickets?: ");
					seniorNum = input.nextInt(); 	
					while(seniorNum < 0) {
						printLine("Invalid number of tickets, please enter a number between 0 and " + totalCols);
						seniorNum = input.nextInt();
					}
					
					// After gathering user info, we check if they're valid inputs, if they are reserve their seats
					boolean isAvailable = checkAvailable(auditorium, rowNum, seatLetter, (adultNum + childNum + seniorNum));
					if(isAvailable) {
						reserveSeats(rowNum, seatLetter, adultNum, seniorNum, childNum);
					}
					else { // otherwise find the best available seats and display them to the user
						System.out.println("no seats available from your selection.");
						// starting seat number of the best available seats to the middle
						int seatNum = (bestAvailable(auditorium, totalCols, (adultNum + childNum + seniorNum), rowNum - 1)
								+ ASCII_CHAR_START);
						//starting and final seat letter is cast into char then string 
						seatLetter = Character.toString((char)(seatNum));
						String finalSeatLetter = Character.toString((char)(seatNum + adultNum + childNum + seniorNum - 1));  
						// checks if the best available method worked
						if(seatNum != (-1 + ASCII_CHAR_START)) {
							printLine("Do you want to reserve the best available seats instead? Type Y or N");
							printLine(rowNum + seatLetter + " - " + rowNum +  finalSeatLetter); // prints the starting and final seats
							// prompt if user wishes to reserve the best available seats
							String yesNo = input.next();
							switch(yesNo) {
								case "Y":
									// best available seats are reserved
									reserveSeats(rowNum, seatLetter, adultNum, seniorNum, childNum);
									break;
								case "N":
									printLine("Refusing best available seats..."); // exit from switch case and returns to the start
									break;
								default: 
									printLine("Please try again, select 'Y' or 'N' to either reserve best available seats or cancel");
									continue;
							}
						}
					}
					break;
				case "2": // when user is prompted to exit
					printLine("Exiting ticket reservation system...");
					EXIT = true;
					break;
				default: // when user makes an error with choice
					printLine("Please enter either 1 to reserve tickets or 2 exit the program.");
					continue;
				
			}
		} 	while (EXIT != true);
		
	}
	public static void main(String[] args) throws IOException {
		
		printLine("What is the name of the file for the auditorium? ");
		// asks for filename
		Scanner input = new Scanner(System.in);
		String filename = input.nextLine();
		// for output file
		PrintWriter output = new PrintWriter(new File("A1.txt"));
		
		// opens file and reads it into array
		openAuditFile(filename);
		// main logic for asking user to reserve tickets and showing them best available seats
		userInterfaceLoop(input, output);
		input.close();
		//output to 2d array contents to file
		outputArrayFile(output);
		output.close();
		// display sales report
		displayReport();
	}

}
