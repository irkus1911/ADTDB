/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adtdb.clases;

import java.io.Serializable;


/**
 *
 * @author 2dam
 */
public class Customer implements Serializable{
    
    private int zip;
    private long id,phone;
    private String firstName,lastName,middleInitial,street,city,state,email;

    public Customer() {  
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void getDatos() {
        System.out.println("---------DATOS CLIENTE---------");
        System.out.println("Id: " + this.getId());
        System.out.println("Ciudad: " + this.getCity());
        System.out.println("Email: " + this.getEmail());
        System.out.println("Nombre: " + this.getFirstName());
        System.out.println("Apellido: " + this.getLastName());
        System.out.println("Inicial del sugundo nombre: " + this.getMiddleInitial());
        System.out.println("Telefono " + this.getPhone());
        System.out.println("Provincia " + this.getState());
        System.out.println("Direccion " + this.getStreet());
        System.out.println("Codigo postal: " + this.getZip());
    }

    
    
}
