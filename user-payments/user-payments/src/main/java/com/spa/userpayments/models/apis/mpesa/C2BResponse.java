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
public class C2BResponse {
    
    @JsonProperty("MerchantRequestID")
    @NotEmpty
    private String merchantRequestID;

    @JsonProperty("CheckoutRequestID")
    @NotEmpty
    private String checkoutRequestID;

    @JsonProperty("ResponseCode")
    @NotEmpty
    private String responseCode;

    @JsonProperty("ResponseDescription")
    @NotEmpty
    private String responseDescription;

    @JsonProperty("CustomerMessage")
    @NotEmpty
    private String customerMessage;


    public String toString(){
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception ex){
            return super.toString();
        }
    }

}
