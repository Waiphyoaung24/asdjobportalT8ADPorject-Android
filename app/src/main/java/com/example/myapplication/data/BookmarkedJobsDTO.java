package com.example.myapplication.data;

import java.io.Serializable;
import java.time.LocalDate;

public class BookmarkedJobsDTO implements Serializable {
    private long id;
    private String jobtitle;
    private String companyname;
    private String bookmarkDate;

    private long jobId;

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public BookmarkedJobsDTO() {}

    public BookmarkedJobsDTO(long id, String jobtitle, String companyname,String bookmarkDate) {
        super();
        this.id = id;
        this.jobtitle = jobtitle;
        this.companyname = companyname;
        this.bookmarkDate = bookmarkDate;
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
