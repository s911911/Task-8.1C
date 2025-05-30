package com.example.task81.api;

import com.google.gson.JsonObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatService {
    @POST("chat")
    Call<JsonObject> sendMessage(@Body JsonObject body);
}
