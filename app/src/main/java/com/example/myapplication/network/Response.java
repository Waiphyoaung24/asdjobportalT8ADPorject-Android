package com.example.myapplication.network;


import com.example.myapplication.data.BookmarkedJobsDTO;
import com.example.myapplication.data.JobAdminDTO;
import com.example.myapplication.data.JobDTO;
import com.example.myapplication.data.ResponseMessage;
import com.example.myapplication.data.ViewedJobsDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
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

}
