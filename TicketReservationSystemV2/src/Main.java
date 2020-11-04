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
	
	// used to round decimals to hundredth place
	private static DecimalFormat df = new DecimalFormat("0.00");
		
	// replaces system.out.println for convenience
	public static void printLine(String str) {
		System.out.println(str);
	}
	// displays final report of tickets, types, and seats sold to user
	public static void displayReport(Auditorium audit) {
		int totalSold = 0, adultSold = 0, childSold = 0, seniorSold = 0;
		double totalSales = 0;
		// Counts num sold by going through each seat and incrementing based on type found
		for(int i = 1; i < audit.getNumRows() + 1; i++) {
			for(int j = 1; j < audit.getNumCols() + 1; j++) { 
				switch(audit.findSeat(i,j).getPayload().getTicketType()) { // get the ticket type from the current position in the linkedlist
					case 'A':
						adultSold++;
						break;
					case 'C':
						childSold++;
						break;
					case 'S':
						seniorSold++;
						break;
				}
			}
		}
		//Calculates and displays the num sold and sub types.
		totalSales = (adultSold * ADULT_TICKET_COST) + (childSold * CHILD_TICKET_COST) + (seniorSold * SENIOR_TICKET_COST);
		totalSold =  (seniorSold + childSold + adultSold);
		System.out.println("Total Seats:	" + (audit.getNumCols() * audit.getNumRows()) + "\n" +
						   "Total Tickets:	" + totalSold + "\n" +
						   "Adult Tickets:	" + adultSold + "\n" +
						   "Child Tickets:	" + childSold + "\n" +
						   "Senior Tickets:	" + seniorSold + "\n" +
						   "Total Sales:	$" + df.format(totalSales)); // rounded to 2 decimal places
	}
	/*
	 * Input Validation Functions
	 */
	// obtain the correct row from user
	public static int getRow(Scanner input, Auditorium audit) {
		int rowChoice = -1;
			try {
				audit.displayAuditorium();
				printLine("Please enter a row number from above: ");
				rowChoice = input.nextInt();
				if(rowChoice > audit.getNumRows() || rowChoice <= 0) {
					printLine("Invalid row number. Try again with a proper input ");
					rowChoice = getRow(input, audit);
				}
			}
			catch (InputMismatchException e) {
				printLine("Invalid row number. Try again with a proper input ");
				input.next();
				rowChoice = getRow(input, audit);
			}
		return rowChoice;
	}
	// Get the correct seat char
	public static String getSeat(Scanner input, Auditorium audit) {
		String seatChoice = "";
		String maxCharacter = Character.toString((char) audit.getNumCols() + ASCII_CHAR_START - 1); // maximum number of characters allowed 
			try {
				printLine("Enter a valid seat letter: ");
				seatChoice = input.next();
				if(seatChoice.length() > 1 || seatChoice.compareTo(maxCharacter) > 0) {
					printLine("Invalid seat letter. Try again with a proper input");
					getSeat(input, audit);
				}
			}
			catch (InputMismatchException e) {
				printLine("Invalid seat letter. Try again with a proper input");
				input.next();
				getSeat(input, audit);
			}
		return seatChoice.toUpperCase();
	}
	// Get valid number of tickets for each type, adult, child, senior
	public static int getTicketNum(Scanner input, String ticketType, Auditorium audit) {
		int ticketNum = -1;
			try {
				printLine("How many " + ticketType + " tickets?: ");
				ticketNum = input.nextInt();
				if(ticketNum < 0) {
					System.out.println("Invalid number of tickets, please enter a number between 0 and " + audit.getNumCols());
					ticketNum = getTicketNum(input, ticketType, audit);
				}
			}
			catch (InputMismatchException e) {
				System.out.println("Invalid type for ticket, please enter a integer between 0 and " + audit.getNumCols());
				input.next();
				ticketNum = getTicketNum(input, ticketType, audit);
			}
		return ticketNum;
	}
	// gets user input for the menu
	public static int getMenuNum(Scanner input) {
		int menuNum = -1;
			try {
				printLine("1. Reserve Seats");
				printLine("2. Exit");
				menuNum = input.nextInt();
				if(menuNum < 0 || menuNum > 2) {
					printLine("Please enter either 1 to reserve tickets or 2 exit the program.");
					menuNum = getMenuNum(input);
				}
			}
			catch (InputMismatchException e) {
				printLine("Please enter either 1 to reserve tickets or 2 exit the program.");
				input.next();
				menuNum = getMenuNum(input);
			}
		return menuNum;
	}
	
	// main logic for asking user to reserve tickets and best available seats
	public static void userInterfaceLoop(Scanner input, Auditorium audit) throws IOException {
		boolean EXIT = false; // used to exit the do loop when user is prompted to exit
		
		do {
			int rowNum = 0, adultNum = 0, childNum = 0, seniorNum = 0; // stores the row number and the num of tickets for each type
			String seatLetter = ""; // used to reserve seats and check if it's available
			// scanner for user selection in menu
			int menuSelect = getMenuNum(input);
			switch (menuSelect) {
				case 1: // when user chooses to reserve seats
					// input validation and prompts to gather information on what seats user wants
					rowNum = getRow(input, audit);
					// only allows capital letters and the ABCs as viable strings to enter
					seatLetter = getSeat(input, audit);
					// gathers ticket amount along with input validation for all sub types
					adultNum = getTicketNum(input, "adult", audit);
					childNum = getTicketNum(input, "child", audit);
					seniorNum = getTicketNum(input, "senior", audit);
					
					int seat = ((int) (seatLetter.charAt(0)))-ASCII_CHAR_START+1; // changes string to char then to integer
					printLine(Integer.toString(seat));
					// After gathering user info, we check if they're valid inputs, if they are reserve their seats
					boolean isAvailable = audit.checkAvailable(rowNum, seat, (adultNum + childNum + seniorNum));
					char seatChar = seatLetter.charAt(0);
					if(isAvailable) { // when available reserve seats
						audit.reserveSeats(rowNum, seatChar, adultNum, seniorNum, childNum);
					}
					else { // otherwise find the best available seats and display them to the user
						System.out.println("There are seats not available from your selection.");
						// starting seat of the best available seats to the middle of the auditorium
						Node<Seat> startSeat = audit.bestAvailable(audit.getNumCols(), (adultNum + childNum + seniorNum), audit.getNumRows());
						// gets the starting set and the best row from the best available method
						seatChar = startSeat.getPayload().getSeat();
						int bestRow = startSeat.getPayload().getRow();
						// add starting seat char with the number of 
					    char finalSeatChar = (char) (seatChar + adultNum + childNum + seniorNum - 1);  
						// checks if the best available method worked
						if(startSeat != null) {
							printLine("Do you want to reserve the best available seats instead? Type Y or N");
							System.out.println(bestRow + Character.toString(seatChar) + " - " + bestRow + finalSeatChar); // prints the starting and final seats
							// prompt if user wishes to reserve the best available seats
							String yesNo = input.next();
							switch(yesNo) {
								case "Y":
									// best available seats are reserved
									audit.reserveSeats(bestRow, seatChar, adultNum, seniorNum, childNum);
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
				case 2: // when user is prompted to exit
					printLine("Exiting ticket reservation system...");
					EXIT = true;
					break;
			}
		} 	while (EXIT != true);
		
	}
	public static void main(String[] args) throws IOException {
		
		printLine("What is the name of the file for the auditorium? ");
		// asks for filename
		Scanner input = new Scanner(System.in);
		String filename = input.nextLine();
		
		// opens file and reads it into a 2d linked list 
		Auditorium audit = new Auditorium(filename);
		// main logic for asking user to reserve tickets and showing them best available seats
		userInterfaceLoop(input, audit);
		input.close();
		//output to 2d array contents to file
		audit.outputListFile();
		// display sales report
		displayReport(audit);
	}

}
