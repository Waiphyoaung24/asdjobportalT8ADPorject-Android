package com.example.myapplication.network;


import com.example.myapplication.data.BookmarkedJobsDTO;
import com.example.myapplication.data.CompaniesReviewDTO;
import com.example.myapplication.data.JobAdminDTO;
import com.example.myapplication.data.ApplicantDTO;
import com.example.myapplication.data.BookmarkedJobsDTO;
import com.example.myapplication.data.JobAdminDTO;
import com.example.myapplication.data.JobDTO;
import com.example.myapplication.data.ResponseMessage;
import com.example.myapplication.data.ReviewDTO;
import com.example.myapplication.data.ViewedJobsDTO;
import com.example.myapplication.data.ResponseMessage;
import com.example.myapplication.data.Token;
import com.example.myapplication.data.UserDTO;
import com.example.myapplication.data.ViewedJobsDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Response {

    String BASE_URL = "http://10.0.2.2:8080/api/";


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

    @GET("jobadmin/details/{id}")
    Call<JobAdminDTO> getJob(@Path("id")Long id);

    @POST("jobadmin/bookmark/{id}")
    Call<BookmarkedJobsDTO> saveBookmark(@Path("id")Long id);

    @POST("jobadmin/applyjoburl/{id}")
    Call<ResponseMessage> ApplyJobUrl(@Path("id")Long id);

    @POST("jobadmin/applyjobemail/{id}")
    Call<JobAdminDTO> ApplyJobEmail(@Path("id")Long id);

    @POST("jobadmin/shareurl/{id}")
    Call<JobAdminDTO> ShareURL(@Path("id")Long id);

    @GET("jobadmin/details/bookmark/list")
    Call<List<BookmarkedJobsDTO>> listBookmarkJobs();

    @GET("jobadmin/details/viewed/list")
    Call<List<ViewedJobsDTO>> ListViewedJobs();



    //review

    @GET("review/list")
    Call<List<ReviewDTO>> getAllReviews();

    @GET("review/company/review/{companyname}")
    Call<List<ReviewDTO>> getReviewsByCompanyName(@Path("companyname") String companyname);

    @POST("review/newreview")
    Call<ReviewDTO> createReview(@Body ReviewDTO rdto);

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

    //account related
    @POST("login")
    @FormUrlEncoded
    Call<Token> login(@Field("username") String username, @Field("password") String password);

    @GET("user/list")
    Call<List<UserDTO>> getUserList();

    @POST("user/admin")
    Call<ResponseBody> saveAdmin(UserDTO user);

    @POST("user/applicant")
    Call<ResponseBody> saveApplicant(ApplicantDTO applicant);

    @DELETE("user/Applicant")
    Call<ResponseBody> deleteApplicant(ApplicantDTO applicant);

    @GET("user/refreshtoken")
    Call<Token> refreshToken();

    @GET("user/applicant/{username}")
    Call<ApplicantDTO> getApplicant(@Header("Authorization") String authHeader, @Path("username")String username);

}
