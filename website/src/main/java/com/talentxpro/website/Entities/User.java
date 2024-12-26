package com.talentxpro.website.Entities;


import com.talentxpro.website.models.Role;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String userName;
    private String userPassword;
    private String userEmailId;
    private boolean isUserTwoFactorEnabled = false;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role userRole;

    //mapping for the applications of the user
    @OneToMany(mappedBy = "user_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();

    private String signUpMethod;

    public User() {
    }

    public User(Long userId, String userName, String userPassword, String userEmailId, boolean isUserTwoFactorEnabled, Role userRole, List<Application> applications) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmailId = userEmailId;
        this.isUserTwoFactorEnabled = isUserTwoFactorEnabled;
        this.userRole = userRole;
        this.applications = applications;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public boolean isUserTwoFactorEnabled() {
        return isUserTwoFactorEnabled;
    }

    public void setUserTwoFactorEnabled(boolean userTwoFactorEnabled) {
        isUserTwoFactorEnabled = userTwoFactorEnabled;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public String getSignUpMethod() {
        return signUpMethod;
    }

    public void setSignUpMethod(String signUpMethod) {
        this.signUpMethod = signUpMethod;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userEmailId='" + userEmailId + '\'' +
                ", isUserTwoFactorEnabled=" + isUserTwoFactorEnabled +
                ", userRole=" + userRole +
                ", applications=" + applications +
                '}';
    }
}
