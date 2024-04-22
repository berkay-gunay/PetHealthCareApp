package com.berkaygunay.finalproject;

import com.google.firebase.Timestamp;

public class PetInfo {
    public String petname;
    public String id;
    public String kindofthispet;
    public String information;
    public Timestamp date;
    public String emailofPetOwner;

    public PetInfo(String petname,String id, String kindofthispet, String information,Timestamp date,String emailofPetOwner) {
        this.petname = petname;
        this.id = id;
        this.kindofthispet = kindofthispet;
        this.information = information;
        this.date=date;
        this.emailofPetOwner = emailofPetOwner;

        //date i textView a duzgun sekilde göstermem lazım.
    }
}
