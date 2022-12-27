package com.spa.userpayments.services.operations;


import com.spa.userpayments.services.commonservices.Utilities;

import com.spa.userpayments.models.apis.mpesa.AuthResponse;
import com.spa.userpayments.models.apis.mpesa.C2BResponse;
import com.spa.userpayments.models.apis.mpesa.C2BRequest;
import com.spa.userpayments.models.apis.Response;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;


import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.HashMap;
import java.time.Instant;


@Service
public class SubOperations{

    // Class injections:

    @Autowired
    Utilities utilities;



    // Auth Credentials injections:

    @Value("${mpesa.sandbox.auth_url}")
    private String auth_url="";

    @Value("${mpesa.sandbox.username}")
    private String auth_username="";

    @Value("${mpesa.sandbox.password}")
    private String auth_password="";


    // Business Credenitals Injections:


    @Value("${spa.business.shortcode}")
    private String shortcode="";

    @Value("${spa.business.password}")
    private String password="";

    @Value("${spa.business.transaction_type}")
    private String transaction_type="";

    @Value("${spa.business.partyB}")
    private String partyB="";

    @Value("${spa.business.callback_url}")
    private String callback_url="";

    @Value("${spa.business.accountReference}")
    private String accountReference="";

    @Value("${spa.business.transaction_description}")
    private String transaction_description="";

    @Value("${spa.sandbox.c2b_url}")
    private String c2b_url="";



    public HashMap<String, String> sendAuthRequest(HashMap<String, String> request){


        // Start Timer:

        Instant startTime = Instant.now();

        String error = "";
    
        utilities.logStart(request.get("request_ref_id"), 
            "sendAuthRequest", request.toString());

        WebClient client = WebClient.create();

        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, String> response = new HashMap<String, String>();



        try{

            // Send Request to Daraja:

            WebClient.ResponseSpec responseSpec = client.get()
                .uri(auth_url)
                .headers(headers -> headers.setBasicAuth(auth_username, auth_password))
                .retrieve();


            // Parse the request to object:

            String responseBody = responseSpec.bodyToMono(String.class).block();

            AuthResponse authResponse = mapper.readValue(responseBody, AuthResponse.class);


            // Response:

            response.put("access_token", authResponse.getAccess_token());
            response.put("expires_in", authResponse.getExpires_in());
            
            response.put("response_status", "success");
            response.put("response_description", "Successfully processed query");



        }catch(Exception e){

            error=e.toString();

            response.put("response_status", "failure");
            response.put("response_description", "Internal Exception");


        }finally{

            Instant endTime = Instant.now();

            utilities.logEnd(request.get("request_ref_id"), 
                "sendAuthRequest", startTime, endTime, request.toString(), response.toString(), error);

        }


        return response;


    }



    public HashMap<String, String> sendC2BRequest(HashMap<String, String> request){


        // Start Timer:

        Instant startTime = Instant.now();

        String error = "";
    
        utilities.logStart(request.get("request_ref_id"), 
            "sendC2BRequest", request.toString());


        ObjectMapper mapper = new ObjectMapper();

        WebClient client = WebClient.create();

        HashMap<String, String> response = new HashMap<String, String>();



        try{

            // Construct Request:

            C2BRequest c2brequest = new C2BRequest();

            c2brequest.setBusinessShortCode(shortcode);
            c2brequest.setPassword(password);
            c2brequest.setTimestamp("20221212115108");
            c2brequest.setTransactionType(transaction_type);
            c2brequest.setAmount(request.get("price"));
            c2brequest.setPartyA(request.get("contact_no"));
            c2brequest.setPartyB(partyB);
            c2brequest.setPhoneNumber(request.get("contact_no"));
            c2brequest.setCallBackURL(callback_url);
            c2brequest.setAccountReference(accountReference);
            c2brequest.setTransactionDesc(transaction_description + request.get("contact_no"));

            String jsonRequest = mapper.writeValueAsString(c2brequest);


            // Send request to Daraja:

            System.out.println("JSON REQUEST: " + jsonRequest);

            String c2bResponse_json = client.post()
                .uri(c2b_url)
                .header("Authorization", ("Bearer " + request.get("access_token")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(jsonRequest.toString()), String.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();


            // Response:

            C2BResponse c2bResponse = mapper.readValue(c2bResponse_json, C2BResponse.class);

            response.put("mpesa_response_code", c2bResponse.getResponseCode());
            response.put("mpesa_response_description", c2bResponse.getResponseDescription());


            response.put("response_status", "success");
            response.put("response_description", "Successfully processed query");



        }catch(Exception e){

            error=e.toString();

            response.put("response_status", "failure");
            response.put("response_description", "Internal Exception");


        }finally{

            Instant endTime = Instant.now();

            utilities.logEnd(request.get("request_ref_id"), 
                "sendC2BRequest", startTime, endTime, request.toString(), response.toString(), error);

        }


        return response;


    }

}
