package ch09.sec05;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class SerializationTweaks {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// Demonstrates writeObject/readObject
		var lp = new LabeledPoint("Rome", 41.902782, 12.496366);
		// Demonstrates writeExternal/readExternal
		var lp2 = new LabeledPixel("bottom right", 1919, 1079);
		// Demonstrates writeReplace/readResolve
		Person p = PersonDatabase.INSTANCE.findById(2);

		try (var out = new ObjectOutputStream(new FileOutputStream("test.ser"))) {
			out.writeObject(lp);
			out.writeObject(lp2);
			out.writeObject(p);
		}

		try (var in = new ObjectInputStream(new FileInputStream("test.ser"))) {
			lp = (LabeledPoint) in.readObject();
			System.out.println(lp);
			lp2 = (LabeledPixel) in.readObject();
			System.out.println(lp2);
			Person q = (Person) in.readObject();
			System.out.println(q);
			System.out.println(p == q);
		}
	}
}
