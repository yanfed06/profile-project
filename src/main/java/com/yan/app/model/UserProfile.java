package com.yan.app.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
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
