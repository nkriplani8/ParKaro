package com.example.parkaro.Models;

public class userData {
    private String username;
    private String userno;
    private String useremail;
    private String password;

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getVehicle_model() {
        return vehicle_model;
    }

    public void setVehicle_model(String vehicle_model) {
        this.vehicle_model = vehicle_model;
    }

    private String vehicle_no;
    private String vehicle_type;
    private String vehicle_model;



    public userData() {
        //no arg constructor for firebase firestore-----
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getUserno() {

        return userno;
    }

    public void setUserno(String userno) {

        this.userno = userno;
    }

    public String getUseremail() {

        return useremail;
    }

    public void setUseremail(String useremail) {

        this.useremail = useremail;
    }

    public String getpassword() {

        return password;
    }

    public void setUserpassword(String password) {

        this.password = password;
    }
}
