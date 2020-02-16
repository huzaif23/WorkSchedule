package com.app.workschedule.Retrofit;

import com.app.workschedule.Model.ResponseFromServerLogin;
import com.app.workschedule.Model.ResponseFromServerSignup;
import com.app.workschedule.Model.ResponseFromServerTaskDetails;
import com.app.workschedule.Model.ResponseFromServerTasks;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface WebService {
    @FormUrlEncoded
    @POST("user_login.php")
    Call<ResponseFromServerLogin> getLoginDetails(@Field("email") String emailId, @Field("password") String password);

    @FormUrlEncoded
    @POST("user_sign_up.php")
    Call<ResponseFromServerSignup> signUpUser(@Field("username") String userName
            , @Field("email") String email
            , @Field("type") String type
            , @Field("password") String password
            , @Field("mobile") String mobile
            , @Field("employer_id") String employerId
    );

    @FormUrlEncoded
    @POST("get_tasks.php")
    Call<ResponseFromServerTasks> getTasksFromServer(@Field("user_id") String userID, @Field("type") String type);

    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    @Multipart
    @POST("add_task.php")
    Call<JsonObject> addTask(
            @Part("user_id") RequestBody userId
            , @Part("assigned_user") RequestBody assignedId
            , @Part("date") RequestBody date
            , @Part("time") RequestBody time
            , @Part("instruction") RequestBody instruction
            , @Part("days_repeat") RequestBody repeat
            , @Part("pictures") RequestBody picture
            , @Part("notify") RequestBody notify
            , @Part MultipartBody.Part file
    );

    @FormUrlEncoded
    @POST("task_status.php")
    Call<JsonObject> markComplete(@Field("task_id") String taskId,@Field("status") String status);

    @Multipart
    @POST("edit_task.php")
    Call<JsonObject> editTask(
            @Part("date") RequestBody date
            , @Part("time") RequestBody time
            , @Part("instruction") RequestBody instruction
            , @Part("days_repeat") RequestBody repeat
            , @Part("pictures") RequestBody picture
            , @Part("notify") RequestBody notify
            , @Part("task_id") int taskId
            , @Part MultipartBody.Part file
    );


}