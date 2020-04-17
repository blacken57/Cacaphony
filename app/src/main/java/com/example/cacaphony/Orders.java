package com.example.cacaphony;

public class Orders {
    private String id;
    private String rest, uName, uPhone;
    private Boolean assigned;
   private double amount;

    public Orders(String id, String rest, String uName, String uPhone, double amount){//*, Boolean assigned*/) {
        this.id = id;
        this.rest = rest;
        this.uName = uName;
        this.uPhone = uPhone;
        this.amount = amount;
      /*  this.assigned = assigned;*/
    }

    public String getId() {
        return id;
    }

    public String getRest() {
        return rest;
    }

    public String getuName() {
        return uName;
    }

    public String getuPhone() {
        return uPhone;
    }

  /*  public String getAssigned(){return assigned.toString();}*/

    public double getAmount() {
        return amount;
    }
}
