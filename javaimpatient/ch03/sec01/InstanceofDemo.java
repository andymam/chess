package ch03.sec01;

public class InstanceofDemo {
	public static void main(String[] args) {
		// A cast
		IntSequence sequence = new DigitSequence(1729);
		DigitSequence digits = (DigitSequence) sequence;
		System.out.println(digits.rest());
		if (Math.random() < 0.5)
			sequence = new SquareSequence();

		// Cannot possibly work—IntSequence is not a supertype of String
		// String digitString = (String) sequence;

	    // Could work, throws a class cast exception if not
		SquareSequence squares = (SquareSequence) sequence;
		
		// The instanceof operator (old school)
		
		if (sequence instanceof DigitSequence) {
		    DigitSequence ds = (DigitSequence) sequence;
		    System.out.println(ds.rest());
		}
		
		// Instanceof with pattern matching
		if (sequence instanceof DigitSequence ds) {
		    // Here, you can use the ds variable
			System.out.println(ds.rest());
		}
		
		// Can use variable in Boolean expression
		if (sequence instanceof DigitSequence ds && ds.rest() >= 100) {
			System.out.println(ds.rest());
		}
		
		// Also with conditional operator
		double rest = sequence instanceof DigitSequence ds ? ds.rest() : 0;
		System.out.println(rest);
	}
}
