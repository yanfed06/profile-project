package com.yan.app.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserProfile {
    private String firstName;
    private String lastName;
    private Date birthday;
    private String school;
    private String hobbies;
    private String achievements;
    private String creation;
    private String studies;
    private String schoolLife;
    private String homeland;
}
