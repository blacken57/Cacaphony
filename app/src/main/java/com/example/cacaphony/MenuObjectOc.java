package com.example.cacaphony;

public class MenuObjectOc {
    private String name;
    private int[] price;
    public MenuObjectOc(String name, int[] price)
    {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int[] getPrice(){
        return price;
    }
}
