package com.spa.userpayments.models.db;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "spa.Payments")
public class Payments {


    @Column(name="payments_id", nullable=true)
    private String payments_id;
    
    @Column(name="user_id", nullable=true)
    private String user_id;
    
    @Column(name="service_id", nullable=true)
    private String service_id;

    @Column(name="contact_no", nullable=true)
    private String contact_no;

    @Column(name="price", nullable=true)
    private String price;

    @Column(name="starting_time", nullable=true)
    private String starting_time;

    @Column(name="ending_time", nullable=true)
    private String ending_time;

    @Column(name="status", nullable=true)
    private String status;

    @Column(name="description", nullable=true)
    private String description;


    public String toString(){
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception ex){
            return super.toString();
        }
    }


}

