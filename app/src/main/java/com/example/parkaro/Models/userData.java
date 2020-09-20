package com.example.parkaro.Models;

public class userData {
    private String username;
    private String userno;
    private String useremail;
    private String password;
    private String licence_no;
    private String car_no;
    private String car_city;
    private String car_state;
    private String car_type;
    private String car_model;

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public userData() {
        //no arg constructor for firebase firestore-----
    }
    public String getLicence_no() {

        return licence_no;
    }

    public void setLicence_no(String licence_no) {

        this.licence_no = licence_no;
    }

    public String getCar_no() {

        return car_no;
    }

    public void setCar_no(String car_no) {

        this.car_no = car_no;
    }

    public String getCar_city() {
        return car_city;
    }

    public void setCar_city(String car_city) {

        this.car_city = car_city;
    }

    public String getCar_state() {

        return car_state;
    }

    public void setCar_state(String car_state) {

        this.car_state = car_state;
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

    public void setUseremail(String useruseremail) {

        this.useremail = useremail;
    }

    public String getpassword() {

        return password;
    }

    public void setUserpassword(String password) {

        this.password = password;
    }
}
