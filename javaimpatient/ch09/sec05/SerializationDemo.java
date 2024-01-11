package ch09.sec05;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class SerializationDemo {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Path path = Path.of("employees.ser");
        try (var out = new ObjectOutputStream(
                Files.newOutputStream(path))) {
            var peter = new Employee("Fred", 90000);
            var paul = new Manager("Barney", 105000);
            var mary = new Manager("Mary", 180000);
            peter.setBoss(mary);
            paul.setBoss(mary);
            out.writeObject(peter);
            out.writeObject(paul);
            
            out.writeObject(new LabeledPoint("origin", 0, 0));
            
            out.writeObject(PersonDatabase.INSTANCE.findById(1));
        }
        try (var in = new ObjectInputStream(
                Files.newInputStream(path))) {
            var e1 = (Employee) in.readObject();
            var e2 = (Employee) in.readObject();
            System.out.println(e1);
            System.out.println(e2);
            
            System.out.println(in.readObject());
            System.out.println(in.readObject());
        }
    }
}
