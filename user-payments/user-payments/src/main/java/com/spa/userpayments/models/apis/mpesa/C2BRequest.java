package com.spa.userpayments.models.apis.mpesa;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.constraints.NotEmpty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;


@Data
@Valid
public class C2BRequest {
    
 
    @JsonProperty("BusinessShortCode")
    @NotEmpty
    private String businessShortCode;

    @JsonProperty("Password")
    @NotEmpty
    private String password;

    @JsonProperty("Timestamp")
    @NotEmpty
    private String timestamp;

    @JsonProperty("TransactionType")
    @NotEmpty
    private String transactionType;

    @JsonProperty("Amount")
    @NotEmpty
    private String amount;

    @JsonProperty("PartyA")
    @NotEmpty
    private String partyA;

    @JsonProperty("PartyB")
    @NotEmpty
    private String partyB;

    @JsonProperty("PhoneNumber")
    @NotEmpty
    private String phoneNumber;

    @JsonProperty("CallBackURL")
    @NotEmpty
    private String callBackURL;

    @JsonProperty("AccountReference")
    @NotEmpty
    private String accountReference;

    @JsonProperty("TransactionDesc")
    @NotEmpty
    private String transactionDesc;


    public String toString(){
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception ex){
            return super.toString();
        }
    }

}
