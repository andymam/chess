package ch09.sec05;

import java.io.Serializable;

public class Person implements Serializable {
    private int id;
    private String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    private Object writeReplace() {
        return new PersonProxy(id);
    }
    
    public String toString() {
        return "%s[id=%d,name=%s]".formatted(getClass().getName(), id, name);
    }
}
