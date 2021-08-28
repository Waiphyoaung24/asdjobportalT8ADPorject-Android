package com.example.myapplication.data;

import java.time.LocalDate;
import java.io.Serializable;

public class ApplicantDTO implements Serializable{
    private Long id;
    private String username; //the username would be email
    private String password;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String avatarImageURl;
    private String roles;
    private String resumeURl;
    private String userStatus;
    private String chatstatus;
    private LocalDate dob;
    private String gender;
    private String selfIntroduction;

    public ApplicantDTO() {
    }

    public ApplicantDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public ApplicantDTO(String username, String password, String firstName, String lastName,String chatstatus) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.chatstatus = chatstatus;
    }


    public ApplicantDTO(Long id, String username, String password, String firstName, String lastName, String contactNumber, String avatarImageURl, String roles, String resumeURl, String userStatus, String chatstatus, LocalDate dob, String gender, String selfIntroduction) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.avatarImageURl = avatarImageURl;
        this.roles = roles;
        this.resumeURl = resumeURl;
        this.userStatus = userStatus;
        this.chatstatus = chatstatus;
        this.dob = dob;
        this.gender = gender;
        this.selfIntroduction = selfIntroduction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAvatarImageURl() {
        return avatarImageURl;
    }

    public void setAvatarImageURl(String avatarImageURl) {
        this.avatarImageURl = avatarImageURl;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getResumeURl() {
        return resumeURl;
    }

    public void setResumeURl(String resumeURl) {
        this.resumeURl = resumeURl;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getChatstatus() {
        return chatstatus;
    }

    public void setChatstatus(String chatstatus) {
        this.chatstatus = chatstatus;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSelfIntroduction() {
        return selfIntroduction;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    @Override
    public String toString() {
        return "ApplicantDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", avatarImageURl='" + avatarImageURl + '\'' +
                ", roles='" + roles + '\'' +
                ", resumeURl='" + resumeURl + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", chatstatus='" + chatstatus + '\'' +
                ", dob=" + dob +
                ", gender='" + gender + '\'' +
                ", selfIntroduction='" + selfIntroduction + '\'' +
                '}';
    }
}
