package com.killua.heartfailuremanagement;

public class Account {
    private String id;
    private String name;
    private String password;
    private int gender;
    private String birthday;
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

    public void setbirthday(String birthday){
        this.birthday=birthday;
    }
    public String getbirthday(){
        return birthday;
    }

    public void settype(int type){
        this.type=type;
    }
    public int gettype(){
        return type;
    }

}
