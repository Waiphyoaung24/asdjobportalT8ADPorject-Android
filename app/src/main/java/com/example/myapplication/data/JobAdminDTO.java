package com.example.myapplication.data;

import java.io.Serializable;

public class JobAdminDTO implements Serializable {

    private long id;
    private String jobTitle;
    private String jobIndustry;
    private String jobqualification;
    private String jobDescription;
    private int autismLevel;
    private float jobStarRating;
    private String jobPositionURL;
    private String companyname;
    private String companyemail;

    public JobAdminDTO() {}

    public JobAdminDTO(long id, String jobTitle, String jobIndustry, String jobqualification, String jobDescription, int autismLevel,
                       float jobStarRating, String jobPositionURL, String companyname,String companyemail) {
        super();
        this.id = id;
        this.jobTitle = jobTitle;
        this.jobIndustry = jobIndustry;
        this.jobqualification = jobqualification;
        this.jobDescription = jobDescription;
        this.autismLevel = autismLevel;
        this.jobStarRating = jobStarRating;
        this.jobPositionURL = jobPositionURL;
        this.companyname = companyname;
        this.companyemail=companyemail;
    }

    public long getId() {
        return id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobIndustry() {
        return jobIndustry;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public int getAutismLevel() {
        return autismLevel;
    }

    public float getJobStarRating() {
        return jobStarRating;
    }

    public String getJobPositionURL() {
        return jobPositionURL;
    }

    public String getCompanyname() {
        return companyname;
    }

    public String getJobqualification() {
        return jobqualification;
    }

    public String getCompanyemail() { return companyemail; }
}
