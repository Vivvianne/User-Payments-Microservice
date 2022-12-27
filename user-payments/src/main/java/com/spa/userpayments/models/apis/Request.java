package com.spa.userpayments.models.apis;


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
public class Request {
    
    @JsonProperty("ServiceRequest")
    @NotEmpty
    private ServiceRequest serviceRequest;


    @Data
    @Valid
    public static class ServiceRequest {

        @NotEmpty
        @JsonProperty(value="Header")
        private Header header;

        @NotEmpty
        @JsonProperty(value="Body")
        private Body body;


        @Data
        @Valid
        public static class Header{

            @NotEmpty
            @JsonProperty(value="RequestRefID")
            private String requestRefID;

            @NotEmpty
            @JsonProperty(value="Timestamp")
            private String timestamp;

            @NotEmpty
            @JsonProperty(value="OperationVersion")
            private String operationVersion;

            @NotEmpty
            @JsonProperty(value="SourceSystem")
            private String sourceSystem;


            public String toString(){
                try {
                    return new ObjectMapper().writeValueAsString(this);
                } catch (Exception ex){
                    return super.toString();
                }
            }


        }


        @Data
        @Valid
        public static class Body {

            @NotEmpty
            @JsonProperty(value="Data")
            private ArrayList <Item> data;

            @Data
            @Valid
            public static class Item {

                @NotEmpty
                @JsonProperty("Key")
                String key;

                @NotEmpty
                @JsonProperty("Value")
                String value;

                public String toString(){
                    try {
                        return new ObjectMapper().writeValueAsString(this);
                    } catch (Exception ex){
                        return super.toString();
                    }
                }

                

            }


            public String toString(){
                try {
                    return new ObjectMapper().writeValueAsString(this);
                } catch (Exception ex){
                    return super.toString();
                }
            }

        }

        public String toString(){
            try {
                return new ObjectMapper().writeValueAsString(this);
            } catch (Exception ex){
                return super.toString();
            }
        }

    }

    public String toString(){
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception ex){
            return super.toString();
        }
    }
}
