package ch02.sec03;

public class EmployeeDemo {
    public static void main(String[] args) {
        var james = new Employee("James Bond", 500000);
            // calls Employee(String, double) constructor
        var anonymous = new Employee("", 40000);
            // calls Employee(double) constructor
        var unpaid = new Employee("Igor Intern");
        var e = new Employee();
            // no-arg constructor
    }
}
