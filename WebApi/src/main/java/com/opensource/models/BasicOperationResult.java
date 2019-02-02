package com.opensource.models;

public class BasicOperationResult<T> {
    public String getMessage() {
        return Message;
    }


    public boolean isSuccess() {
        return Success;
    }

    public T getResult() {
        return Result;
    }

    public BasicOperationResult(String message, boolean success, T result) {
        Message = message;
        Success = success;
        Result = result;
    }

    private String Message;
    private boolean Success;
    private T Result;
}
