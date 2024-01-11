package ch02.sec04;

import java.util.Date;

record Point(double x, double y) {
	// A custom constructor
	public Point() {
		this(0, 0);
	}

	// A method
	public double distanceFromOrigin() {
		return Math.hypot(x, y);
	}

	// A static field and method (see Section 2.5)
	public static Point ORIGIN = new Point();

	public static double distance(Point p, Point q) {
		return Math.hypot(p.x - q.x, p.y - q.y);
	}
}

record PointInTime(double x, double y, Date when) {
}

record Range(int from, int to) {
	// A compact constructor
	public Range {
		if (from > to) { // Swap the bounds		
			int temp = from;
			from = to;
			to = temp;
		}
	}
}

public class RecordDemo {
	public static void main(String[] args) {
		var p = new Point(3, 4);
		// Accessors 
		System.out.println("Coordinates of p: " + p.x() + " " + p.y());
		Point q = new Point();
		System.out.println(p + " " + q); // toString is provided

		System.out.println("Distance from origin: " + p.distanceFromOrigin());
		// Same computation with static field and method
		System.out.println("Distance from origin: " + Point.distance(Point.ORIGIN, p));

		// A mutable record
		var pt = new PointInTime(3, 4, new Date());
		System.out.println("Before: " + pt);
		pt.when().setTime(0);
		System.out.println("After: " + pt);

		// The compact constructor "prelude" swaps the arguments
		var r = new Range(4, 3);
		System.out.println("r: " + r);
	}
}

