/*
 * Name: Matthew Vu
 * Class: CS 2336
 * ID: MSV180000
 * Purpose: Ticket reservation system for a movie theater. Uses file i/o to make
 * reservations and outputs results to output file.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Auditorium {
	private Node<Seat> First;
	private int numRows;
	private int numCols;
	
	// constructor for auditorum
	Auditorium(String filename) throws FileNotFoundException  {
		File file = new File(filename);
		int row = 1; // first row
		char seat = 'A'; // first seat
		String line; // for each line of the file
		
		try {
			if(file.canRead()) { // if file can be read continue
				Scanner s = new Scanner(file);
				
				line = s.nextLine(); // get the first line of the auditorium
				
				int colLen = line.length(); // get the number of cols
					
				// create curr and temporary left ptrs 
				Node<Seat> current = null;
				Node<Seat> tempLeft = null;
				
				// creates the first row of the 2d linkedList
				for(int i = 0; i < colLen; i++) {
					current = new Node<Seat>(new Seat(row, seat, line.charAt(i))); // create the node at the next column
					if(i == 0) {
						setFirst(current); // point the head ptr to the node
						tempLeft = getFirst(); // point the temp ptr to the head ptr
					}
					current.setLeft(tempLeft); // connect the current node to the previous node
					tempLeft.setRight(current); // connect the prev node to the next node
					tempLeft = current; // point the templeft pointer to the current node
					seat++;
				}
				
				// used to help point up and down nodes to each other
				Node<Seat> tempUp = getFirst();
				Node<Seat> firstSeat = getFirst();
				
				while(s.hasNextLine()) {
					line = s.nextLine(); // gets next line of file
					// reset the ptrs and variables
					tempLeft = null; 
					seat = 'A';
					row++;
					// create the rest of the rows for the linked list and connecting thm to the one above
					for(int i = 0; i < colLen; i++) {
						current = new Node<Seat>(new Seat(row, seat, line.charAt(i))); // create a current node
						current.setLeft(tempLeft); // connect node with prev left 
						current.setUp(tempUp); // connect currentf node with node above it
						tempUp.setDown(current); // connect upper node with current node
						if(i != 0) { // connect the prev left node with the current
							tempLeft.setRight(current); 
						}
						tempLeft = current; // prev left ptr points to current 
						tempUp = tempUp.getRight(); // point the upper node to the right of it
						seat++; 
					}
					firstSeat = firstSeat.getDown(); // move the head ptr down one node
					tempUp = firstSeat; 
				}
				numRows = row; 
				numCols = seat - 'A';
				s.close(); // close the file
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	// mutators and accessors
	public Node<Seat> getFirst() {
		return this.First;
	}
	public void setFirst(Node<Seat> first) {
		this.First = first;
	}
	public int getNumRows() {
		return numRows;
	}
	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}
	public int getNumCols() {
		return numCols;
	}
	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}
	// helper method: generates the header for the display auditorium and the valid seats available
	public void rowLetterGenerator() {
			System.out.print("  ");
			for(int i = 0; i < numCols; i++) {
				char letter = (char)(i+65); // cast int to ascii char
				System.out.print(letter); 
			}
			System.out.println();
		}
	
	//Finds the seat the seat given the row and char
	Node<Seat> findSeat(int row, char seat)
		{
		    Node<Seat> seatNeeded = getFirst();
		    
		    //Goes through each row to get to the wanted row
		    for(int i = 1; i < row; i++) {
		        seatNeeded = seatNeeded.getDown();
		    }
		    //Goes through each seat to get to the wanted row
		    for(int j = 1; j < seat-'A'+1; j++) {
		        seatNeeded = seatNeeded.getRight();
		    }
		    return seatNeeded;
		}
	//Finds the seat the seat given the row and seat
	Node<Seat> findSeat(int row, int seat)
	{
	    Node<Seat> seatNeeded = getFirst();
	    
	    //Goes through each row to get to the wanted row
	    for(int i = 1; i < row; i++) {
	        seatNeeded = seatNeeded.getDown();
	    }
	    //Goes through each seat to get to the wanted row
	    for(int j = 1; j < seat; j++) {
	        seatNeeded = seatNeeded.getRight();
	    }
	    return seatNeeded;
	}

	// displays what the auditorium looks like to user
	public void displayAuditorium() {
		int count = 0;
		Node<Seat> current = getFirst();
		Node<Seat> down = current.getDown();
		
		rowLetterGenerator();
		while(current != null) {
			System.out.print(count+1 + " "); // prints number on the left hand side of the columns
			while(current != null) { // until reach end of rull
				if(current.getPayload().getTicketType() != '.') { // print # instead of the letters for occupied seats
					System.out.print("#");
				}
				else {
					System.out.print(".");
				}
					current = current.getRight(); // move on to next node in row
				}
				// move on to next row
				current = down;
				if(down != null) {
					down = down.getDown();
					System.out.println();
				}
				count++; // increment the number of rows on the side
			}
			System.out.println();
		} 	
	// helper function to reserve seats, checks if seat is available
	public boolean checkAvailable(int row, int seat, int totalQuantity) {
		Node<Seat> seatNeeded = findSeat(row, seat); // finds the given seat
		
		for(int i = 0; i < totalQuantity; i++) { // loop through the same amount seats the user reserved
			if(seatNeeded == null || seatNeeded.getPayload().getTicketType() != '.') { // check if seat is null or . 
				return false;
			}
			seatNeeded = seatNeeded.getRight(); 
		}
		return true;
	}
	
	//allows user to reserve seats (child, senior, adult) checks if they are available
	public void reserveSeats(int row, char seat, int adultQuantity, int seniorQuantity, int childQuantity) {	
		Node<Seat> reservedSeat = findSeat(row, seat);
		// goes through each ticket type and reserves in order of adults, children, and seniors
		for(int i = 0; i < adultQuantity; i++) {
			reservedSeat.setPayload(new Seat(row, seat, 'A')); // set the seat to A for adults
			reservedSeat = reservedSeat.getRight();
		}
		for(int j = 0; j < childQuantity; j++) {
			reservedSeat.setPayload(new Seat(row, seat, 'C')); // set the seat to A for adults
			reservedSeat = reservedSeat.getRight();
		}
		for(int k = 0; k < seniorQuantity; k++) {
			reservedSeat.setPayload(new Seat(row, seat, 'S')); // set the seat to A for adults
			reservedSeat = reservedSeat.getRight();
		}
	}
		// method outputs 2d linkedList to output file A1.txt
	public void outputListFile() throws IOException {
		// for output file
		PrintWriter output = new PrintWriter(new File("A1.txt"));
		// pointers for traversing the list
		Node<Seat> current = getFirst();
		Node<Seat> down = current.getDown();
		
		// go through all the rows
		while(current != null) {
			// go through all the of the seats in the rows and print them
			while(current != null) {
				output.print(current.getPayload().getTicketType());
				current = current.getRight();
			}
			current = down; // move down one node
			
			if(down != null) { // move down one node if there is more
				down = down.getDown();
				output.println();
			}
		}
		output.close(); // close file
	}
		
}
