package com.example.myapplication.data;

import java.io.Serializable;
import java.time.LocalDate;

public class BookmarkedJobsDTO implements Serializable {
    private long id;
    private String jobtitle;
    private String companyname;
    private String bookmarkDate;

    private long jobDetailId;



    public BookmarkedJobsDTO() {}

    public BookmarkedJobsDTO(long id, String jobtitle, String companyname,String bookmarkDate,long jobDetailId) {
        super();
        this.id = id;
        this.jobtitle = jobtitle;
        this.companyname = companyname;
        this.bookmarkDate = bookmarkDate;
        this.jobDetailId = jobDetailId;
    }
    public long getJobId() {
        return jobDetailId;
    }

    public void setJobId(long jobId) {
        this.jobDetailId = jobId;
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

    public String getBookmarkDate() {
        return bookmarkDate;
    }
}
