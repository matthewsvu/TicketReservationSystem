import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Auditorium {
	private Node<Seat> First;
	// constructor for auditorum
	Auditorium(String filename) throws FileNotFoundException  {
		Scanner s = new Scanner(new File(filename));
		
		s.close();
	}
	// mutators and accessors
	public Node<Seat> getFirst() {
		return First;
	}
	public void setFirst(Node<Seat> first) {
		this.First = first;
	}
}
