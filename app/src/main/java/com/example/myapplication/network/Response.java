package com.example.myapplication.network;


import com.example.myapplication.data.JobDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Response {

    String BASE_URL = "http://10.0.2.2:8080/api/job/";

    @GET("list")
    Call<List<JobDTO>>getAllJobs();

    @GET("categorize/{categorizeId}")
    Call<List<JobDTO>>getCateogryByIndustry(@Path("categorizeId")String categorizeId);

    @GET("category")
    Call<List<String>>getCateoryList();

    @GET("search/{word}")
    Call<List<JobDTO>>getJobsByTitleOrDesc(@Path("word")String word);

    @GET("filter/{title}/{jobStarRating}/{autismLvl}")
    Call<List<JobDTO>>filterJobs(@Path("title")String title,@Path("jobStarRating")float jobStarRating,@Path("autismLvl")int autismLvl);

}
