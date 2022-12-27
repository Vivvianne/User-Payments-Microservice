package com.spa.userpayments.services.operations;

import com.spa.userpayments.services.commonservices.Utilities;

import com.spa.userpayments.models.db.Payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.time.Instant;
import java.util.List;  

@Service
public class DBOperations {

    // Class Injections:

    @Autowired
    Utilities utilities;



    // JDBC injections:

    @Autowired
    private JdbcTemplate jdbc;



    public HashMap<String, String> savePayment(HashMap<String, String> request){


        // Start Timer:

        Instant startTime = Instant.now();

        String error = "";
    
        utilities.logStart(request.get("request_ref_id"), 
            "savePayments", request.toString());


        HashMap<String, String> response = new HashMap<String, String>();



        try{


            jdbc.update( "insert into spa.Payments(payments_id, user_id, service_id, contact_no, price, starting_time, ending_time, status, description, created_on, updated_on) values (?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, current_timestamp)",
                request.get("request_ref_id"),
                request.get("user_id"),
                request.get("service_id"),
                request.get("contact_no"),
                request.get("price"),
                request.get("starting_time"),
                request.get("ending_time"),
                "processing",
                "Request is processing"
                );


            response.put("response_status", "success");
            response.put("response_description", "Successfully processed query");



        }catch(Exception e){

            error=e.toString();

            response.put("response_status", "failure");
            response.put("response_description", "Internal Exception");


        }finally{

            Instant endTime = Instant.now();

            utilities.logEnd(request.get("request_ref_id"), 
                "savePayments", startTime, endTime, request.toString(), response.toString(), error);

        }


        return response;



    }


    public HashMap<String, String> fetchPaymentDetails(HashMap<String, String> request){


        // Start Timer:

        Instant startTime = Instant.now();

        String error = "";
    
        utilities.logStart(request.get("request_ref_id"), 
            "fetchPaymentDetails", request.toString());


        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, String> response = new HashMap<String, String>();


        try{



            String sql = "select * from spa.Payments where payments_id=?";

            Payments user = (Payments) jdbc.queryForObject(
                sql, new Object[]{request.get("payment_id")}, new BeanPropertyRowMapper(Payments.class));


            response.put("response_status", "success");
            response.put("response_description", "Successfully processed query");



            String payment_details_json = mapper.writeValueAsString(user);


            response.put("payment_details", payment_details_json);


        }catch(Exception e){

            error=e.toString();

            response.put("response_status", "failure");
            response.put("response_description", "Internal Exception");


        }finally{

            Instant endTime = Instant.now();

            utilities.logEnd(request.get("request_ref_id"), 
                "fetchPaymentDetails", startTime, endTime, request.toString(), response.toString(), error);

        }


        return response;


    
    }

   public HashMap<String, String> checkPaymentisPresent(HashMap<String, String> request){

        // Start Timer:

        Instant startTime = Instant.now();

        String error = "";
    
        utilities.logStart(request.get("request_ref_id"), "checkPaymentisPresent", request.toString());


        HashMap<String, String> response = new HashMap<String, String>();



        try{


            String sql = "SELECT count(*) FROM spa.Payments WHERE payments_id = ?";


            int count = jdbc.queryForObject( sql, new Object[] { request.get("payment_id") }, Integer.class);


            response.put("response_status", "success");
            response.put("response_description", "Successfully processed query");


            response.put("payment_count", String.valueOf(count));


        }catch(Exception e){

            error=e.toString();

            response.put("response_status", "failure");
            response.put("response_description", "Internal Exception");


        }finally{

            Instant endTime = Instant.now();

            utilities.logEnd(request.get("request_ref_id"), 
                "checkPaymentisPresent", startTime, endTime, request.toString(), response.toString(), error);

        }


        return response;

    }



    public HashMap<String, String> fetchAllPayments(HashMap<String, String> request){


        // Start Timer:

        Instant startTime = Instant.now();

        String error = "";
    
        utilities.logStart(request.get("request_ref_id"), 
            "fetchAllPayments", request.toString());


        HashMap<String, String> response = new HashMap<String, String>();

        ObjectMapper mapper = new ObjectMapper();



        try{


            String sql = "select * from spa.Payments";

            List<Payments> services = jdbc.query( sql, new BeanPropertyRowMapper(Payments.class));

    
            String payment_details_json = mapper.writeValueAsString(services);


            response.put("response_status", "success");
            response.put("response_description", "Successfully processed query");


            response.put("payment_details", payment_details_json);



        }catch(Exception e){

            error=e.toString();

            response.put("response_status", "failure");
            response.put("response_description", "Internal Exception");


        }finally{

            Instant endTime = Instant.now();

            utilities.logEnd(request.get("request_ref_id"), 
                "fetchAllPayments", startTime, endTime, request.toString(), response.toString(), error);

        }


        return response;


    }


}