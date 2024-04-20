package com.example.mpi_project1;

public class panier_model {
    public panier_model() {
        // Default constructor
    }
    String name,email,price,durl;
    public panier_model(String name,String email,String price,String durl){
        this.name=name;
        this.email=email;
        this.price=price;
        this.durl=durl;
    }

    public String getDurl() {
        return durl;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPrice() {
        return price;
    }
}
