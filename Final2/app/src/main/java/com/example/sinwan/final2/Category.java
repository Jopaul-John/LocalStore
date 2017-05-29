package com.example.sinwan.final2;

public class Category {
	
	private int id;
	private String name,lat,lon;
	
	public Category(){}
	
	public Category(String name){
		this.id = id;
		this.name = name;
		this.lat=lat;
		this.lon=lon;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
    public String getLat(){
        return this.lat;
    }
    public String getLon(){
        return this.lon;
    }
}
