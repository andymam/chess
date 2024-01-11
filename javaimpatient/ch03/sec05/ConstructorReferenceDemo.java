package ch03.sec05;

import java.util.List;

public class ConstructorReferenceDemo {
    public static void main(String[] args) {
        var names = List.of("Peter", "Paul", "Mary");
        Employee[] employees = names.stream().map(Employee::new).toArray(Employee[]::new);
        for (Employee e : employees) System.out.println(e.getName());
    }
}
