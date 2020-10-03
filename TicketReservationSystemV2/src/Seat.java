
public class Seat {
	private int row;
	private char seat;
	private char ticketType;
	// default constructor
	Seat() {
	}
	// overloaded constructor
	Seat(int r, char s, char tType) {
		this.row = r;
		this.seat = s;
		this.ticketType = tType;
	}
	// mutators and accessors
	public int getRow() {
		return row;
	}
	public void setRow(int r) {
		this.row = r;
	}
	public char getSeat() {
		return seat;
	}
	public void setSeat(char s) {
		this.seat = s;
	}
	public char getTicketType() {
		return ticketType;
	}
	public void setTicketType(char tType) {
		this.ticketType = tType;
	}
}
