package com.example.mpi_project1;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class product_model {
    public String name,prix,purl,id,domain;
    public List<String> list_of_images=new ArrayList<>();
    public List<String> listString=new ArrayList<>();
    public String email,discription;
    product_model(){
    }
    public product_model(String imageurl_profile,String name,String prix,List<String> imageURL,String email,String discription,String id,String domain){
        this.name=name;
        this.prix=prix;
        this.purl=imageurl_profile;
        this.list_of_images=imageURL;
        this.email=email;
        this.discription=discription;
        this.id=id;
        this.domain=domain;
    }
    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public List<String> getList_of_images() {
        for(String uri:list_of_images){
            listString.add(uri.toString());
        }
        return listString;
    }
    public String getDiscription() {
        return discription;
    }
    public String getPrix() {
        return prix;
    }

    public String getPurl() {
        return purl;
    }

    public String getId() {
        return id;
    }

    public String getDomain() {
        return domain;
    }
}
