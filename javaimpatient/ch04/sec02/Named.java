package ch04.sec02;

public interface Named {
    default String getName() { return ""; }
}