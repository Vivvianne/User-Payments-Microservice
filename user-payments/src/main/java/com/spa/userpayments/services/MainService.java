package com.spa.userpayments.services;


import com.spa.userpayments.models.apis.Response;


import java.util.HashMap;


public interface MainService {

    // Payments:
    
    public Response makePaymentOperation(HashMap<String, String> request);
    public Response fetchPaymentOperation(HashMap<String, String> request);
    public Response fetchAllPaymentsOperation(HashMap<String, String> request);
    

}
