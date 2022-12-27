package com.spa.userpayments.models.apis;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.Valid;

import java.util.ArrayList;


@Data
public class Response {

    @JsonProperty("ServiceResponse")
    private ServiceResponse serviceResponse;


    @Data
    public static class ServiceResponse {

        @JsonProperty("ResponseHeader")
        private Header header;

        @JsonProperty("Body")
        private Body body;


        @Data
        public static class Header{


            @JsonProperty("RequestRefID")
            private String responseRefID;

            @JsonProperty("ResponseCode")
            private String responseCode;

            @JsonProperty("ResponseMsg")
            private String responseMsg;

            @JsonProperty("DetailedMsg")
            private String detailedMsg;


            public String toString(){
                try {
                    return new ObjectMapper().writeValueAsString(this);
                } catch (Exception ex){
                    return super.toString();
                }
            }

        }


        @Data
        public static class Body {

            @NotEmpty
            @JsonProperty(value="Data")
            private ArrayList <Item> data;

            @Data
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