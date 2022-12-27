package com.spa.userpayments.controllers;


import com.spa.userpayments.models.apis.Response;
import com.spa.userpayments.models.apis.Request;

import com.spa.userpayments.services.commonservices.Utilities;
import com.spa.userpayments.services.MainService;



import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.Valid;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.time.Instant;




@RestController
@RequestMapping("/api/spa/userpayments")
public class MainController {


    // Class injections:

    @Autowired
    MainService mainService;

    @Autowired
    Utilities utilities;


    
    // Response injections:

    @Value("${userpayments.responsecode.internal_exception}")
    private String responsecode_internal_exception="";

    @Value("${userpayments.responsemsg.failure}")
    private String responsemsg_failure="";

    @Value("${userpayments.detailedmsg.internal_exception}")
    private String detailedmsg_internal_exception="";




    // Users Methods:

    @PostMapping("/create")
    public Response makePayment(@Valid @RequestBody Request request){


        // Start Timer:

        Instant startTime = Instant.now();

        Response response = new Response();

        String error = "";
    
        utilities.logStart(request.getServiceRequest().getHeader().getRequestRefID().toString(), 
            "makePayment", request.toString());

        HashMap<String, String> mainservice_request = new HashMap<>();
        

        response.setServiceResponse(new Response.ServiceResponse());
        response.getServiceResponse().setHeader(new Response.ServiceResponse.Header());
        response.getServiceResponse().setBody(new Response.ServiceResponse.Body());
        



        try{

            
            mainservice_request.put("request_ref_id", request.getServiceRequest().getHeader().getRequestRefID());
            
            mainservice_request.put("user_id", request.getServiceRequest().getBody().getData().get(0).getValue().toString());
            mainservice_request.put("service_id", request.getServiceRequest().getBody().getData().get(1).getValue().toString());
            mainservice_request.put("contact_no", request.getServiceRequest().getBody().getData().get(2).getValue().toString());
            mainservice_request.put("price", request.getServiceRequest().getBody().getData().get(3).getValue().toString());
            mainservice_request.put("starting_time", request.getServiceRequest().getBody().getData().get(4).getValue().toString());
            mainservice_request.put("ending_time", request.getServiceRequest().getBody().getData().get(5).getValue().toString());

            response = mainService.makePaymentOperation(mainservice_request);



        }catch(Exception e){



            error=e.toString();

            response.getServiceResponse().getHeader().setResponseCode(responsecode_internal_exception);
            response.getServiceResponse().getHeader().setResponseMsg(responsemsg_failure);
            response.getServiceResponse().getHeader().setDetailedMsg(detailedmsg_internal_exception);


        }finally{

            Instant endTime = Instant.now();

            response.getServiceResponse().getHeader().setResponseRefID(request.getServiceRequest().getHeader().getRequestRefID());

            utilities.logEnd(request.getServiceRequest().getHeader().getRequestRefID().toString(), 
                "makePayment", startTime, endTime, request.toString(), response.toString(), error);

        }


        return response;


    }




    @PostMapping("/fetch")
    public Response fetchPayment(@Valid @RequestBody Request request){

         // Start Timer:

        Instant startTime = Instant.now();

        Response response = new Response();

        String error = "";
    
        utilities.logStart(request.getServiceRequest().getHeader().getRequestRefID().toString(), 
            "fetchPayment", request.toString());

        HashMap<String, String> mainservice_request = new HashMap<>();
        

        response.setServiceResponse(new Response.ServiceResponse());
        response.getServiceResponse().setHeader(new Response.ServiceResponse.Header());
        response.getServiceResponse().setBody(new Response.ServiceResponse.Body());
        


        try{

            
            mainservice_request.put("request_ref_id", request.getServiceRequest().getHeader().getRequestRefID());
            
            mainservice_request.put("payment_id", request.getServiceRequest().getBody().getData().get(0).getValue().toString());
          
            response = mainService.fetchPaymentOperation(mainservice_request);




        }catch(Exception e){



            error=e.toString();

            response.getServiceResponse().getHeader().setResponseCode(responsecode_internal_exception);
            response.getServiceResponse().getHeader().setResponseMsg(responsemsg_failure);
            response.getServiceResponse().getHeader().setDetailedMsg(detailedmsg_internal_exception);


        }finally{

            Instant endTime = Instant.now();

            response.getServiceResponse().getHeader().setResponseRefID(request.getServiceRequest().getHeader().getRequestRefID());

            utilities.logEnd(request.getServiceRequest().getHeader().getRequestRefID().toString(), 
                "fetchPayment", startTime, endTime, request.toString(), response.toString(), error);

        }


        return response;


    }


    @PostMapping("/fetchall")
    public Response fetchAllPayments(@Valid @RequestBody Request request){

        // Start Timer:

        Instant startTime = Instant.now();

        Response response = new Response();

        String error = "";
    
        utilities.logStart(request.getServiceRequest().getHeader().getRequestRefID().toString(), 
            "fetchAllPayments", request.toString());

        HashMap<String, String> mainservice_request = new HashMap<>();
        

        response.setServiceResponse(new Response.ServiceResponse());
        response.getServiceResponse().setHeader(new Response.ServiceResponse.Header());
        response.getServiceResponse().setBody(new Response.ServiceResponse.Body());
        


        try{

            
            mainservice_request.put("request_ref_id", request.getServiceRequest().getHeader().getRequestRefID());
                 
            response = mainService.fetchAllPaymentsOperation(mainservice_request);


        }catch(Exception e){



            error=e.toString();

            response.getServiceResponse().getHeader().setResponseCode(responsecode_internal_exception);
            response.getServiceResponse().getHeader().setResponseMsg(responsemsg_failure);
            response.getServiceResponse().getHeader().setDetailedMsg(detailedmsg_internal_exception);


        }finally{

            Instant endTime = Instant.now();

            response.getServiceResponse().getHeader().setResponseRefID(request.getServiceRequest().getHeader().getRequestRefID());

            utilities.logEnd(request.getServiceRequest().getHeader().getRequestRefID().toString(), 
                "fetchAllPayments", startTime, endTime, request.toString(), response.toString(), error);

        }

        return response;


    }


    @PostMapping("/callback")
    public void mpesaCallback(String request){

        System.out.println("REQUEST: " + request);

    }


}
