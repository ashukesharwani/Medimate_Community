package com.example.medimate;

public class ModelUsers {

    private String Aadhar,BloodGroup,City,Email,FName,Locality,Phone;

    public String getAadhar() {
        return Aadhar;
    }

    public void setAadhar(String aadhar) {
        this.Aadhar = aadhar;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.BloodGroup = bloodGroup;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        this.City = city;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getFname() {
        return FName;
    }

    public void setFname(String fname) {
        this.FName = fname;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        this.Locality = locality;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public ModelUsers(){}

    private ModelUsers(String aadhar, String bloodGroup, String city, String email, String fname, String locality, String phone) {
        this.Aadhar = aadhar;
        this.BloodGroup = bloodGroup;
        this.City = city;
        this.Email = email;
        this.FName = fname;
        this.Locality = locality;
        this.Phone = phone;
    }





}
