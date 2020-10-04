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
	/*
	//Finds the bestAvailable seats
	public static Seat bestAvailableTest(Auditorium auditorium, int seatsInRow, int totalQuantity, int rowNum)
	{
	    //Create marker variables
	    int bestRow=-1;
	    int bestCol=-1;
	    double bestDistance = Integer.MAX_VALUE;
	    double tempDistance = 0.0;
	    double mid=((double)(totalQuantity-1)/2.0);
	    
	    
	    double midRow=((double)(rowNum+1)/2.0);
	    double midCol=((double)(seatsInRow+1)/2.0);
	    //Goes through the rows
	    for(int r = 1; r <= rowNum; r++)
	    {
	        //Goes through the cols
	        for(int c = 1; c <= seatsInRow; c++)
	        {
	            //First checks if the seat is availible
	            if(checkAvailable(r, c, totalQuantity))
	            { 
	                //Calculates the distance
	                tempDistance = Math.sqrt(Math.pow(r - midRow, 2) + Math.pow(c + mid - midCol, 2));
	                // calculates distances from middle
	                double currRowDistance = Math.abs(r - Math.ceil((double)(rowNum)/2.0));
	                double bestRowDistance	= Math.abs(bestRow - Math.ceil((double)(rowNum)/2.0));	
	                //Checks if the distance is smaller
	                if(bestDistance>tempDistance)
	                {
	                    bestDistance=tempDistance;
	                    bestRow=r;
	                    bestCol=c;
	                }
	                //Checks of the distance is equal
	                else if(bestDistance==tempDistance)
	                {
	                    //Checks which row is closer to the middle
	                    if(currRowDistance < bestRowDistance)
	                    {
	                        bestDistance=tempDistance;
	                        bestRow=r;
	                        bestCol=c;
	                    }
	                    //Checks if the distance to the middle row is equal
	                    else if(currRowDistance == bestRowDistance)
	                    {
	                        //Chooses the lower row
	                        if(r < bestRow)
	                        {
	                            bestDistance = tempDistance;
	                            bestRow = r;
	                            bestCol = c;
	                        }
	                    }
	                }
	                
	            }
	        }
	    }
	    if(bestCol == -1 && bestRow == -1)
	        return null;
	    return findSeat(bestRow,bestCol);
	}
	*/
	
	
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
	
	
	// main logic for asking user to reserve tickets and best available seats
	public static void userInterfaceLoop(Scanner input, Auditorium audit) throws IOException {
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
					audit.displayAuditorium();
					// input validation and prompts to gather information on what seats user wants
					printLine("Please enter a row number from above: ");
					rowNum = input.nextInt();
					while(rowNum <= 0 || rowNum > audit.getNumRows()) {
						audit.displayAuditorium();
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
					int seat = ((int) (seatLetter.charAt(0)))-ASCII_CHAR_START+1; // changes string to char then to integer
					printLine(Integer.toString(seat));
					// After gathering user info, we check if they're valid inputs, if they are reserve their seats
					boolean isAvailable = audit.checkAvailable(rowNum, seat, (adultNum + childNum + seniorNum));
					if(isAvailable) {
						char seatChar = seatLetter.charAt(0);
						audit.reserveSeats(rowNum, seatChar, adultNum, seniorNum, childNum);
					}
					else { // otherwise find the best available seats and display them to the user
						System.out.println("There are seats not available from your selection.");
						/*
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
									audit.reserveSeats(rowNum, seatLetter, adultNum, seniorNum, childNum);
									break;
								case "N":
									printLine("Refusing best available seats..."); // exit from switch case and returns to the start
									break;
								default: 
									printLine("Please try again, select 'Y' or 'N' to either reserve best available seats or cancel");
									continue;
							}
						}
						*/
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
