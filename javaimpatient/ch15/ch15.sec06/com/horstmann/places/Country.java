package com.horstmann.places;

public class Country {
   private String name;
   private double area;

   public Country() {}
   
   public Country(String name, double area) {
      this.name = name;
      this.area = area;
   }

   public String getName() {
      return name;
   }

   public double getArea() {
      return area;
   }
}
