package com.example.myapplication.data;

import com.google.gson.annotations.SerializedName;

public class CompaniesReviewDTO {

    @SerializedName("companyName")
    private String companyName;
    @SerializedName("ratings")
    private float ratings;
    @SerializedName("review")
    private String review;
    @SerializedName("user")
    private String user;
    @SerializedName("userRating")
    private float userRating;
    @SerializedName("jobTitle")
    private String jobTitle;

    public CompaniesReviewDTO(String companyName, float ratings) {
        this.companyName = companyName;
        this.ratings = ratings;
    }

    public CompaniesReviewDTO(String companyName, float ratings, String review, String user, float userRating, String jobTitle) {
        this.companyName = companyName;
        this.ratings = ratings;
        this.review = review;
        this.user = user;
        this.userRating = userRating;
        this.jobTitle = jobTitle;
    }

    public CompaniesReviewDTO() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public float getRatings() {
        return ratings;
    }

    public void setRatings(float ratings) {
        this.ratings = ratings;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyName='" + companyName + '\'' +
                ", ratings=" + ratings +
                '}';
    }
}
