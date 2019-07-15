package com.example.madcamp_3;

public class Alarm {

    String number;
    String message;

    public String getnumber(){return number;}

    public String getMessage(){return message;}

    public Alarm(String number,String message){
        this.message=message;
        this.number=number;
    }
}
