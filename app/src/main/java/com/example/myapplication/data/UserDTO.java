package com.example.myapplication.data;

public class UserDTO {
    private Long id;
    private String username; //the username would be email
    private String password;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String avatarImageURl;
    private String roles;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String password, String firstName, String lastName, int age, String contactNumber, String avatarImageURl, String roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.avatarImageURl = avatarImageURl;
        this.roles = roles;
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

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", avatarImageURl='" + avatarImageURl + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }
}
