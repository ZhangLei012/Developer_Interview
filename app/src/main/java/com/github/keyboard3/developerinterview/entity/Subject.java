package com.github.keyboard3.developerinterview.entity;

public class Subject {
    private String name;
    private int imageId;
    public Subject(String name,int imageId){
        this.name=name;
        this.imageId=imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
