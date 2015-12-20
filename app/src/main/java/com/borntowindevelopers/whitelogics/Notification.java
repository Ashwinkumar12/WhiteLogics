package com.borntowindevelopers.whitelogics;

public class Notification {

    int _id;
    String _cleanBody;
    String _cleanSubject;
    String _receivedTime;

    // Empty constructor
    public Notification(){

    }
    // constructor
    public Notification(int id, String subject, String body, String time){
        this._id = id;
        this._cleanSubject = subject;
        this._cleanBody = body;
        this._receivedTime = time;
    }

    // constructor
/*    public Notification(String name, String _phone_number){
        this._name = name;
        this._phone_number = _phone_number;
    }
*/    // getting ID
    public int getId(){
        return this._id;
    }

    // setting id
    public void setId(int id){
        this._id = id;
    }



    public String getBody(){
        return this._cleanBody;
    }

    // setting id
    public void setBody(String body){
        this._cleanBody = body;
    }

    // getting name
    public String getSubject(){
        return this._cleanSubject;
    }

    // setting name
    public void setSubject(String subject){
        this._cleanSubject = subject;
    }

    // getting phone number
    public String getTime(){ return this._receivedTime; }

    // setting phone number
    public String setTime(String time){
        this._receivedTime = time;
        return time;
    }
}
