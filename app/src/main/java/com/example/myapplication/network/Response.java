package com.example.myapplication.network;


import com.example.myapplication.data.ApplicantDTO;
import com.example.myapplication.data.BookmarkedJobsDTO;
import com.example.myapplication.data.CompaniesReviewDTO;
import com.example.myapplication.data.JobAdminDTO;
import com.example.myapplication.data.JobDTO;
import com.example.myapplication.data.ResponseMessage;
import com.example.myapplication.data.ReviewDTO;
import com.example.myapplication.data.Token;
import com.example.myapplication.data.UserDTO;
import com.example.myapplication.data.ViewedJobsDTO;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface Response {

    String BASE_URL = "http://54.81.148.245:8080/webapp/api/";


    // job

    @GET("job/list")
    Call<List<JobDTO>>getAllJobs();

    @GET("job/categorize/{categorizeId}")
    Call<List<JobDTO>>getCateogryByIndustry(@Path("categorizeId")String categorizeId);

    @GET("job/category")
    Call<List<String>>getCateoryList();

    @GET("job/search/{word}")
    Call<List<JobDTO>>getJobsByTitleOrDesc(@Path("word")String word);

    @GET("job/filter/{title}/{jobStarRating}/{autismLvl}")
    Call<List<JobDTO>>filterJobs(@Path("title")String title,@Path("jobStarRating")float jobStarRating,@Path("autismLvl")int autismLvl);


    //job admin

    @GET("job/details/{id}")
    Call<JobAdminDTO> getJob(@Path("id")Long id);

    @POST("job/bookmark/{id}")
    Call<BookmarkedJobsDTO> saveBookmark(@Path("id")Long id,@Header("Authorization") String authHeader);

    @POST("job/applyjoburl/{id}")
    Call<ResponseMessage> ApplyJobUrl(@Path("id")Long id,@Header("Authorization") String authHeader);

    @POST("job/applyjobemail/{id}")
    Call<JobAdminDTO> ApplyJobEmail(@Path("id")Long id,@Header("Authorization") String authHeader);

    @POST("job/shareurl/{id}")
    Call<JobAdminDTO> ShareURL(@Path("id")Long id,@Header("Authorization") String authHeader);

    @GET("job/details/bookmark/list")
    Call<List<BookmarkedJobsDTO>> listBookmarkJobs(@Header("Authorization") String authHeader);

    @GET("job/details/viewed/list")
    Call<List<ViewedJobsDTO>> ListViewedJobs(@Header("Authorization") String authHeader);



    //review

    @GET("review/list")
    Call<List<ReviewDTO>> getAllReviews();

    @GET("review/company/review/{companyname}")
    Call<List<ReviewDTO>> getReviewsByCompanyName(@Path("companyname") String companyname);

    @POST("review/newreview")
    Call<ReviewDTO> createReview(@Body ReviewDTO rdto,@Header("Authorization") String authHeader);

    @DELETE("review/deletereview/{reviewid}")
    Call<Void> deleteReview(@Path("reviewid") Long reviewid);

    @GET("review/user/review/{userid}")
    Call<List<ReviewDTO>> getReviewsByUser(@Path("userid") Long userid);

    @GET("review/job/review/{jobtitle}")
    Call<List<ReviewDTO>> getReviewsByJob(@Path("jobtitle") String jobTitle);

    @GET("review/job/company/{jobtitle}/{companyname}")
    Call<List<ReviewDTO>> getReviewsByJobandCompany(@Path("jobtitle") String jobTitle,@Path("companyname") String companyname);

    @GET("review/review/list")
    Call<List<CompaniesReviewDTO>> getAllCompanyReviews();

    @GET("review/list/reviews/{reviewid}/{reviewstatus}")
    Call<Void>updateReview(@Path("reviewid") Long reviewid,@Path("reviewstatus") String reviewstatus);


    //account related

    @POST("login")
    @FormUrlEncoded
    Call<Token> login(@Field("username") String username, @Field("password") String password);

    @POST("user/applicant")
    Call<ApplicantDTO> saveApplicant(@Body ApplicantDTO applicant);

    @GET("user/applicant/{username}")
    Call<ApplicantDTO> getApplicant(@Header("Authorization") String authHeader, @Path("username") String username);

    @GET("user/userlist")
    Call<List<ApplicantDTO>> getAllApplicant();

    @POST("user/applicant/update")
    Call<ApplicantDTO> updateApplicant(@Header("Authorization") String authHeader, @Body ApplicantDTO applicant);

    @DELETE("user/applicant/{username}")
    Call<ApplicantDTO> deleteApplicant(@Header("Authorization") String authHeader, @Path("username") String username);

    @GET("user/refreshtoken")
    Call<Token> refreshToken(@Header("Authorization") String authHeader);

    @Streaming
    @GET("user/applicant/avatar/{username}")
    Call<ResponseBody> downloadAvatar(@Header("Authorization")String authorization,@Path("username") String username);

    @Streaming
    @POST("user/applicant/avatar/{username}")
    Call<ResponseBody> uploadAvatar(@Header("Authorization")String authorization, @Part MultipartBody.Part filePart, String username_);
}
