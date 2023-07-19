package com.RWA.rwa.User;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String First_Name;
    private String Last_Name;
    private String User_Name;
    private String Mail;
    private String CellNumber;
    private String Passwd;

    public User(String first_Name, String last_Name, String user_Name, String passwd) {
        First_Name = first_Name;
        Last_Name = last_Name;
        User_Name = user_Name;
        Passwd = passwd;
    }

    public User(int id, String first_Name, String last_Name, String user_Name, String mail, String cellNumber, String passwd) {
        this.id = id;
        First_Name = first_Name;
        Last_Name = last_Name;
        User_Name = user_Name;
        Mail = mail;
        CellNumber = cellNumber;
        Passwd = passwd;
    }

    public User() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String last_Name) {
        Last_Name = last_Name;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public String getCellNumber() {
        return CellNumber;
    }

    public void setCellNumber(String cellNumber) {
        CellNumber = cellNumber;
    }

    public String getPasswd() {
        return Passwd;
    }

    public void setPasswd(String passwd) {
        Passwd = passwd;
    }

}
