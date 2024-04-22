package com.berkaygunay.finalproject;

import java.io.Serializable;

public class RegistrationInfo implements Serializable {
    public String nameSurname;
    public String clinicName;
    public String diplomaNumber;
    public String phoneNumber;
    public String email;
    public String city;
    public String district;
    public String address;

    public RegistrationInfo(String nameSurname, String clinicName, String diplomaNumber, String phoneNumber, String email, String city, String district, String address) {
        this.nameSurname = nameSurname;
        this.clinicName = clinicName;
        this.diplomaNumber = diplomaNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.city = city;
        this.district = district;
        this.address = address;
    }


}
