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
public class AuthResponse {
    
 
    @JsonProperty("access_token")
    @NotEmpty
    private String access_token;

    @JsonProperty("expires_in")
    @NotEmpty
    private String expires_in;


    public String toString(){
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception ex){
            return super.toString();
        }
    }

}
