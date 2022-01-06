package com.yan.app.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class UserProfile {
    private UUID uuid;
    private String firstName;
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String school;
    private String hobbies;
    private String achievements;
    private String creation;
    private String studies;
    private String schoolLife;
    private String homeland;
}
