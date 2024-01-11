package ch04.sec03;

import java.util.Objects;

public class DiscountedItem extends Item {
    private double discount;

    public DiscountedItem(String description, double price, double discount) {
        super(description, price);
        this.discount = discount;
    }

    public boolean equals(Object otherObject) {
        if (!super.equals(otherObject)) return false;
        var other = (DiscountedItem) otherObject;
        return discount == other.discount;
    }
    
    public int hashCode() {
        return Objects.hash(super.hashCode(), discount);
    }
}
