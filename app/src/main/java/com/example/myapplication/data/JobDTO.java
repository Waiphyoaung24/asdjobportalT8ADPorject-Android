package com.example.myapplication.data;

import com.google.gson.annotations.SerializedName;

public class JobDTO {
    @SerializedName("id")
    private long id;

    @SerializedName("jobTitle")
    private String jobTitle;

    @SerializedName("jobIndustry")
    private String jobIndustry;

    @SerializedName("jobDescription")
    private String jobDescription;


    @SerializedName(("jobPositionURL"))
    private String jobPositionURL;

    public float getJobStarRating() {
        return jobStarRating;
    }

    public void setJobStarRating(float jobStarRating) {
        this.jobStarRating = jobStarRating;
    }

    @SerializedName("jobStarRating")
    private float jobStarRating;

    public String getJobPositionURL() {
        return jobPositionURL;
    }

    public void setJobPositionURL(String jobPositionURL) {
        this.jobPositionURL = jobPositionURL;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public JobDTO(long jobid, String jobTitle, String jobIndustry) {
        this.id = jobid;
        this.jobTitle = jobTitle;
        this.jobIndustry = jobIndustry;
    }

    public long getId() {
        return id;
    }

    public void setId(long jobid) {
        this.id = jobid;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobIndustry() {
        return jobIndustry;
    }

    public void setJobIndustry(String jobIndustry) {
        this.jobIndustry = jobIndustry;
    }
}
