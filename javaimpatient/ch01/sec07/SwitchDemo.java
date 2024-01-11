package ch01.sec07;

public class SwitchDemo {
	public static void demo(int seasonCode) {
		System.out.printf("seasonCode=%d%n", seasonCode);		

		// switch expression
		String seasonName = switch (seasonCode) { 
	    	case 0 -> "Spring";
	    	case 1 -> "Summer";
	    	case 2 -> "Fall";
	    	case 3 -> "Winter";
	    	default -> { System.out.println("???"); yield ""; }
		};
		System.out.println(seasonName);
		
		// switch statement
		switch (seasonCode) { // switch statement
	    	case 0 -> seasonName = "Spring";
	    	case 1 -> seasonName = "Summer";
	    	case 2 -> seasonName = "Fall";
	    	case 3 -> seasonName = "Winter";
	    	default -> { System.out.println("???"); seasonName = ""; }
		}
		System.out.println(seasonName);
	
		// Multiple labels
		int numLetters = switch (seasonName) {
	    	case "Spring", "Summer", "Winter" -> 6;
	    	case "Fall" -> 4;
	    	default -> throw new IllegalArgumentException();
		};
		System.out.println(numLetters);
		
		// switch expression with fall-through
		numLetters = switch (seasonName) { 
	    	case "Spring":
	    		System.out.println("spring time!");
	    	case "Summer", "Winter":
	    		yield 6;
	    	case "Fall":
	    		yield 4;
	    	default:
	    		throw new IllegalArgumentException();
		};
		System.out.println(numLetters);
		
		// switch statement with fall-through
		switch (seasonName) { 
		    case "Spring":
		      System.out.println("spring time!");
		    case "Summer", "Winter":
		      numLetters = 6;
		      break;
		   case "Fall":
		      numLetters = 4;
		      break;
		   default:
			  throw new IllegalArgumentException();
		}
		System.out.println(numLetters);		
	}
	
	public static void main(String[] args) {
		demo(0);
		demo(1);
		demo(2);
		demo(3);
		demo(4); // Will throw an exception
	}
}
