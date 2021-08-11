package com.example.myapplication.data;

import com.google.gson.annotations.SerializedName;

public class ResponseMessage {
    @SerializedName("message")
    String message;

    public void setMessage(String message){ this.message= message; }
    public String getMessage() { return message; }

}
