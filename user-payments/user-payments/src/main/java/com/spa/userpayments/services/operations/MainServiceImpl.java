package com.spa.userpayments.services.operations;

import com.spa.userpayments.services.commonservices.Utilities;

import com.spa.userpayments.services.MainService;

import com.spa.userpayments.models.apis.Response;
import com.spa.userpayments.models.db.Payments;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.Instant;
import java.util.List;  


@Service
public class MainServiceImpl implements MainService{

    
    // Class injections:

    @Autowired
    Utilities utilities;

    @Autowired
    DBOperations dbOperations;

    @Autowired
    SubOperations subOperations;

    
    // Response injections:

    @Value("${userpayments.responsecode.success}")
    private String responsecode_success="";

    @Value("${userpayments.responsemsg.success}")
    private String responsemsg_success="";

    @Value("${userpayments.detailedmsg.success}")
    private String detailedmsg_success="";


    @Value("${userpayments.responsecode.internal_exception}")
    private String responsecode_internal_exception="";

    @Value("${userpayments.responsemsg.failure}")
    private String responsemsg_failure="";

    @Value("${userpayments.detailedmsg.internal_exception}")
    private String detailedmsg_internal_exception="";



    @Value("${userpayments.responsecode.failed_to_save_request_to_db}")
    private String responsecode_failed_to_save_request_to_db="";

    @Value("${userpayments.detailedmsg.failed_to_save_request_to_db}")
    private String detailedmsg_failed_to_save_request_to_db="";


    @Value("${userpayments.responsecode.rejected_auth_request}")
    private String responsecode_rejected_auth_request="";

    @Value("${userpayments.detailedmsg.rejected_auth_request}")
    private String detailedmsg_rejected_auth_request="";


    @Value("${userpayments.responsecode.rejected_mpesa_request}")
    private String responsecode_rejected_mpesa_request="";

    @Value("${userpayments.detailedmsg.rejected_mpesa_request}")
    private String detailedmsg_rejected_mpesa_request="";

    @Value("${userpayments.responsecode.payment_not_present}")
    private String responsecode_payment_not_present="";

    @Value("${userpayments.detailedmsg.payment_not_present}")
    private String detailedmsg_payment_not_present="";



    
    public Response makePaymentOperation(HashMap<String, String> request){

        // Start Timer:

        Instant startTime = Instant.now();

        String error = "";
    
        utilities.logStart(request.get("request_ref_id"), 
            "makePaymentOperation", request.toString());


        
        Response response = new Response();
        
        response.setServiceResponse(new Response.ServiceResponse());
        response.getServiceResponse().setHeader(new Response.ServiceResponse.Header());
        response.getServiceResponse().setBody(new Response.ServiceResponse.Body());
    

        try{


            // Save request to db:

            HashMap<String, String> savePayment_response = dbOperations.savePayment(request);


            if(savePayment_response.get("response_status").equals("success")){


                // Generate auth credentials.

                HashMap<String, String> generateAuthCredentials_response = subOperations.sendAuthRequest(request);


                if(generateAuthCredentials_response.get("response_status").equals("success")){


                    // Send request to mpesa.

                    request.put("access_token" , generateAuthCredentials_response.get("access_token"));
                    request.put("expires_in" , generateAuthCredentials_response.get("expires_in"));
                     

                    HashMap<String, String> sendC2BRequest_response = subOperations.sendC2BRequest(request);


                    if(sendC2BRequest_response.get("response_status").equals("success")){


                        response.getServiceResponse().getHeader().setResponseCode(responsecode_success);
                        response.getServiceResponse().getHeader().setResponseMsg(responsemsg_success);
                        response.getServiceResponse().getHeader().setDetailedMsg(detailedmsg_success);


                    }else{

                        // Rejected Mpesa Request:

                        response.getServiceResponse().getHeader().setResponseCode(responsecode_rejected_mpesa_request);
                        response.getServiceResponse().getHeader().setResponseMsg(responsemsg_failure);
                        response.getServiceResponse().getHeader().setDetailedMsg(detailedmsg_rejected_mpesa_request);

                    }

                }else{

                    // Rejected Auth request:

                    response.getServiceResponse().getHeader().setResponseCode(responsecode_rejected_auth_request);
                    response.getServiceResponse().getHeader().setResponseMsg(responsemsg_failure);
                    response.getServiceResponse().getHeader().setDetailedMsg(detailedmsg_rejected_auth_request);

                }

            }else{

                // Cannot save request to db:

                response.getServiceResponse().getHeader().setResponseCode(responsecode_failed_to_save_request_to_db);
                response.getServiceResponse().getHeader().setResponseMsg(responsemsg_failure);
                response.getServiceResponse().getHeader().setDetailedMsg(detailedmsg_failed_to_save_request_to_db);


            }



        }catch(Exception e){

            error=e.toString();

            response.getServiceResponse().getHeader().setResponseCode(responsecode_internal_exception);
            response.getServiceResponse().getHeader().setResponseMsg(responsemsg_failure);
            response.getServiceResponse().getHeader().setDetailedMsg(detailedmsg_internal_exception);


        }finally{

            Instant endTime = Instant.now();

            utilities.logEnd(request.get("request_ref_id"), 
                "makePaymentOperation", startTime, endTime, request.toString(), response.toString(), error);

        }


        return response;

    }

    public Response fetchPaymentOperation(HashMap<String, String> request){

        // Start Timer:

        Instant startTime = Instant.now();

        Response response = new Response();

        String error = "";
    
        utilities.logStart(request.get("request_ref_id"), "fetchPayment", request.toString());

        ObjectMapper mapper = new ObjectMapper();

        
        response.setServiceResponse(new Response.ServiceResponse());
        response.getServiceResponse().setHeader(new Response.ServiceResponse.Header());
        response.getServiceResponse().setBody(new Response.ServiceResponse.Body());
        


        try{


            // Check if the service exists:

            HashMap<String, String> checkPaymentisPresent_response = dbOperations.checkPaymentisPresent(request);

            if(checkPaymentisPresent_response.get("response_status").equals("success") && 
                    checkPaymentisPresent_response.get("payment_count").equals("1")){


                // Fetch service details:

                HashMap<String, String> fetchPaymentDetails_response = dbOperations.fetchPaymentDetails(request);


                if(fetchPaymentDetails_response.get("response_status").equals("success")){

                    
                    Payments payment = mapper.readValue(fetchPaymentDetails_response.get("payment_details"), 
                        Payments.class);


                    // Extract the payment details:

                    Response.ServiceResponse.Body.Item payment_id_item = new Response.ServiceResponse.Body.Item();
                    payment_id_item.setKey("payments_id");
                    payment_id_item.setValue(payment.getPayments_id());

                    Response.ServiceResponse.Body.Item service_id_item = new Response.ServiceResponse.Body.Item();
                    service_id_item.setKey("service_id");
                    service_id_item.setValue(payment.getService_id());

                    Response.ServiceResponse.Body.Item contact_no_item = new Response.ServiceResponse.Body.Item();
                    contact_no_item.setKey("contact_no");
                    contact_no_item.setValue(payment.getContact_no());

                    Response.ServiceResponse.Body.Item price_item = new Response.ServiceResponse.Body.Item();
                    price_item.setKey("price");
                    price_item.setValue(payment.getPrice());

                    Response.ServiceResponse.Body.Item starting_time_item = new Response.ServiceResponse.Body.Item();
                    starting_time_item.setKey("starting_time");
                    starting_time_item.setValue(payment.getStarting_time());
                    
                    Response.ServiceResponse.Body.Item ending_time_item = new Response.ServiceResponse.Body.Item();
                    ending_time_item.setKey("ending_time");
                    ending_time_item.setValue(payment.getEnding_time());

                    Response.ServiceResponse.Body.Item status_item = new Response.ServiceResponse.Body.Item();
                    status_item.setKey("status");
                    status_item.setValue(String.valueOf(payment.getStatus()));

                    Response.ServiceResponse.Body.Item description_item = new Response.ServiceResponse.Body.Item();
                    description_item.setKey("description");
                    description_item.setValue(payment.getDescription());

                    ArrayList <Response.ServiceResponse.Body.Item> data = new ArrayList <Response.ServiceResponse.Body.Item>();


                    data.add(payment_id_item);
                    data.add(service_id_item);
                    data.add(contact_no_item);
                    data.add(starting_time_item);
                    data.add(ending_time_item);
                    data.add(status_item);
                    data.add(description_item);

                        
                    response.getServiceResponse().getBody().setData(data);
    

                    response.getServiceResponse().getHeader().setResponseCode(responsecode_success);
                    response.getServiceResponse().getHeader().setResponseMsg(responsemsg_success);
                    response.getServiceResponse().getHeader().setDetailedMsg(detailedmsg_success);

                }else{


                    // Internal Exception:

                    response.getServiceResponse().getHeader().setResponseCode(responsecode_internal_exception);
                    response.getServiceResponse().getHeader().setResponseMsg(responsemsg_failure);
                    response.getServiceResponse().getHeader().setDetailedMsg(detailedmsg_internal_exception);


                }


            }else{

                // Service Doesn't Exists:


                response.getServiceResponse().getHeader().setResponseCode(responsecode_payment_not_present);
                response.getServiceResponse().getHeader().setResponseMsg(responsemsg_failure);
                response.getServiceResponse().getHeader().setDetailedMsg(detailedmsg_payment_not_present);

            }

            

        }catch(Exception e){



            error=e.toString();

            response.getServiceResponse().getHeader().setResponseCode(responsecode_internal_exception);
            response.getServiceResponse().getHeader().setResponseMsg(responsemsg_failure);
            response.getServiceResponse().getHeader().setDetailedMsg(detailedmsg_internal_exception);


        }finally{

            Instant endTime = Instant.now();

            utilities.logEnd(request.get("request_ref_id"), "fetchServiceOperation", startTime, 
                endTime, request.toString(), response.toString(), error);

        }


        return response;


    }

    public Response fetchAllPaymentsOperation(HashMap<String, String> request){

        // Start Timer:

        Instant startTime = Instant.now();

        Response response = new Response();

        String error = "";
    
        utilities.logStart(request.get("request_ref_id"), "fetchAllPaymentsOperation", request.toString());

        ObjectMapper mapper = new ObjectMapper();

        
        response.setServiceResponse(new Response.ServiceResponse());
        response.getServiceResponse().setHeader(new Response.ServiceResponse.Header());
        response.getServiceResponse().setBody(new Response.ServiceResponse.Body());
        


        try{


            // Fetch service details:

            HashMap<String, String> fetchAllPayments_response = dbOperations.fetchAllPayments(request);


            if(fetchAllPayments_response.get("response_status").equals("success")){



                List<Payments> payment_list = mapper.readValue(fetchAllPayments_response.get("payment_details"), 
                    new TypeReference<List<Payments>>() {});



                // Extract the service details:

                ArrayList <Response.ServiceResponse.Body.Item> data = new ArrayList <Response.ServiceResponse.Body.Item>();

                for ( Payments payment: payment_list){

                    Response.ServiceResponse.Body.Item payment_id_item = new Response.ServiceResponse.Body.Item();
                    payment_id_item.setKey("payments_id");
                    payment_id_item.setValue(payment.getPayments_id());

                    Response.ServiceResponse.Body.Item service_id_item = new Response.ServiceResponse.Body.Item();
                    service_id_item.setKey("service_id");
                    service_id_item.setValue(payment.getService_id());

                    Response.ServiceResponse.Body.Item contact_no_item = new Response.ServiceResponse.Body.Item();
                    contact_no_item.setKey("contact_no");
                    contact_no_item.setValue(payment.getContact_no());

                    Response.ServiceResponse.Body.Item price_item = new Response.ServiceResponse.Body.Item();
                    price_item.setKey("price");
                    price_item.setValue(payment.getPrice());

                    Response.ServiceResponse.Body.Item starting_time_item = new Response.ServiceResponse.Body.Item();
                    starting_time_item.setKey("starting_time");
                    starting_time_item.setValue(payment.getStarting_time());
                    
                    Response.ServiceResponse.Body.Item ending_time_item = new Response.ServiceResponse.Body.Item();
                    ending_time_item.setKey("ending_time");
                    ending_time_item.setValue(payment.getEnding_time());

                    Response.ServiceResponse.Body.Item status_item = new Response.ServiceResponse.Body.Item();
                    status_item.setKey("status");
                    status_item.setValue(String.valueOf(payment.getStatus()));

                    Response.ServiceResponse.Body.Item description_item = new Response.ServiceResponse.Body.Item();
                    description_item.setKey("description");
                    description_item.setValue(payment.getDescription());


                    data.add(payment_id_item);
                    data.add(service_id_item);
                    data.add(contact_no_item);
                    data.add(starting_time_item);
                    data.add(ending_time_item);
                    data.add(status_item);
                    data.add(description_item);

                }

                    
                response.getServiceResponse().getBody().setData(data);


                response.getServiceResponse().getHeader().setResponseCode(responsecode_success);
                response.getServiceResponse().getHeader().setResponseMsg(responsemsg_success);
                response.getServiceResponse().getHeader().setDetailedMsg(detailedmsg_success);

            }else{


                // Internal Exception:

                response.getServiceResponse().getHeader().setResponseCode(responsecode_internal_exception);
                response.getServiceResponse().getHeader().setResponseMsg(responsemsg_failure);
                response.getServiceResponse().getHeader().setDetailedMsg(detailedmsg_internal_exception);


            }



            

        }catch(Exception e){



            error=e.toString();

            response.getServiceResponse().getHeader().setResponseCode(responsecode_internal_exception);
            response.getServiceResponse().getHeader().setResponseMsg(responsemsg_failure);
            response.getServiceResponse().getHeader().setDetailedMsg(detailedmsg_internal_exception);


        }finally{

            Instant endTime = Instant.now();

            utilities.logEnd(request.get("request_ref_id"), "fetchAllPaymentsOperation", startTime, 
                endTime, request.toString(), response.toString(), error);

        }


        return response;

    }
    
    
    public void MpesaCallback(){


    }


}
