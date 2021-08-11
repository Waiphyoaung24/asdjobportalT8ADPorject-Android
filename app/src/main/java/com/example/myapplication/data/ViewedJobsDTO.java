package com.example.myapplication.data;

import java.io.Serializable;
import java.time.LocalDate;

public class ViewedJobsDTO implements Serializable {
    private long id;
    private String jobtitle;
    private String companyname;
    private String appliedDate;

    public ViewedJobsDTO() { }

    public ViewedJobsDTO(long id, String jobtitle, String companyname, String appliedDate) {
        super();
        this.id = id;
        this.jobtitle = jobtitle;
        this.companyname = companyname;
        this.appliedDate = appliedDate;
    }

    public long getId() {
        return id;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public String getCompanyname() {
        return companyname;
    }

    public String getAppliedDate() {
        return appliedDate;
    }
}
