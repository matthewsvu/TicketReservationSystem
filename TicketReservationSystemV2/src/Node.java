/*
 * Name: Matthew Vu
 * Class: CS 2336
 * ID: MSV180000
 * Purpose: Ticket reservation system for a movie theater. Uses file i/o to make
 * reservations and outputs results to output file.
 */
public class Node<Seat> {
	private Node<Seat> Up;
	private Node<Seat> Down;
	private Node<Seat> Left;
	private Node<Seat> Right;
	private Seat Payload;
	
	// default constructor
	Node() {
		Up = null;
		Down = null;
		Left = null;
		Right = null;
	}
	// overloaded constructor
	Node(Seat pLoad) {
		Up = null;
		Down = null;
		Left = null;
		Right = null;
		Payload = pLoad;
	}
	// mutators and accessors
	public Seat getPayload() {
		return this.Payload;
	}
	public void setPayload(Seat payload) {
		this.Payload = payload;
	}
	public Node<Seat> getRight() {
		return Right;
	}
	public void setRight(Node<Seat> right) {
		Right = right;
	}
	public Node<Seat> getLeft() {
		return Left;
	}
	public void setLeft(Node<Seat> left) {
		Left = left;
	}
	public Node<Seat> getDown() {
		return Down;
	}
	public void setDown(Node<Seat> down) {
		Down = down;
	}
	public Node<Seat> getUp() {
		return Up;
	}
	public void setUp(Node<Seat> up) {
		Up = up;
	}
}
