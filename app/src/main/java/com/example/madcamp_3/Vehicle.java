package com.example.madcamp_3;

public class Vehicle {
    String _id;
    String name;
    String vehicle;
    String number;

    public String get_id(){ return _id;}

    public String getName(){ return name;}

    public  String getVehicle(){ return vehicle; }

    public  String getNumber(){ return number;}

    public Vehicle(String _id,String vehicle,String number){
        this.vehicle=vehicle;
        this.number=number;
        this._id=_id;
    }

}
