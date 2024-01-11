package ch07.sec05;

public record Job(int priority, String description) implements Comparable<Job> {   
    public int compareTo(Job other) {
        return priority - other.priority;
    }
}
