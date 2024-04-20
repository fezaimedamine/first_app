package com.example.mpi_project1;

public class delivre_Model {
    public String name,email,region,durl;
    delivre_Model(){
    }
    public delivre_Model(String durl,String email,String name,String region){
        this.name=name;
        this.email=email;
        this.region=region;
        this.durl=durl;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRegion() {
        return region;
    }

    public String getDurl() {
        return durl;
    }
}
