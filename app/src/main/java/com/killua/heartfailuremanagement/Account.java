package com.killua.heartfailuremanagement;

public class Account {
    private String id;
    private String name;
    private String password;
    private int gender;
    private String birth;
    private int type;

    public Account(){}

    public void setid(String id){
        this.id=id;
    }
    public String getid(){
        return id;
    }

    public void setname(String name){
        this.name=name;
    }
    public String getname(){
        return name;
    }

    public void setpassword(String password){
        this.password=password;
    }
    public String getpassword(){
        return password;
    }

    public void setgender(int gender){
        this.gender=gender;
    }
    public int getgender(){
        return gender;
    }

    public void setbirth(String birth){
        this.birth=birth;
    }
    public String getbirth(){
        return birth;
    }

    public void settype(int type){
        this.type=type;
    }
    public int gettype(){
        return type;
    }

}
