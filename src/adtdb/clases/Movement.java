/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adtdb.clases;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author 2dam
 */
public class Movement implements Serializable{
    
    private long id;
    private Timestamp databaseDate;
    private float amount,balance;
    private String description;

    public Movement() {
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getDatabaseDate() {
        return databaseDate;
    }

    public void setDatabaseDate(Timestamp databaseDate) {
        this.databaseDate = databaseDate;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public  void getDatos(){
        System.out.println(this.getId());
        System.out.println(this.getDescription());
        System.out.println(this.getAmount());
        System.out.println(this.getBalance());
        System.out.println(this.getDatabaseDate());
    }
    
    
    
    
}
