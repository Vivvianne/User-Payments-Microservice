package com.spa.userpayments.services.commonservices;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;


import java.time.temporal.ChronoUnit;
import java.time.Instant;


@Slf4j
@Service
public class Utilities{


    @Async
   public void logStart(String request_ref_id, String operation_name, String request){

        log.info(" RequestRefID = {} | Method={} | Status={} | Request={} ", request_ref_id, operation_name, "Starting Operation", request);

    }   

    @Async
    public void logEnd(String request_ref_id, String operation_name, Instant startTime, Instant endTime, String request, 
        String response, String error){

        log.info(" RequestRefID = {} | Method={} | Status={} | Transaction Cost={} | Request={} | Response={} | Error={}", 
            request_ref_id, operation_name, "Finished Operation", String.valueOf(ChronoUnit.MILLIS.between(startTime, endTime)), request, response, error);

    } 


}
