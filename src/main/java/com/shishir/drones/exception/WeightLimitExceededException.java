package com.shishir.drones.exception;

public class WeightLimitExceededException extends Exception{
    public WeightLimitExceededException(String message){
        super(message);
    }
}
