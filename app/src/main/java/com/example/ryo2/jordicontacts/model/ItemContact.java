package com.example.ryo2.jordicontacts.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by ryo2 on 24/04/2018.
 */

public class ItemContact extends RealmObject {
    @PrimaryKey
    private long id;

    @Required
    private String name;

    @Required
    private String number;

    private int edad;
    private boolean genero;

    public ItemContact(){}
    public ItemContact(long idExt,String nameExt,String numberExt){
        id = idExt;
        name = nameExt;
        number = numberExt;
    }
    public ItemContact(long idExt,String nameExt,String numberExt,int edadExt,boolean generoExt){
        id = idExt;
        name = nameExt;
        number = numberExt;
        edad = edadExt;
        genero = generoExt;
    }

    //seters getters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public boolean isGenero() {
        return genero;
    }

    public void setGenero(boolean genero) {
        this.genero = genero;
    }
}
