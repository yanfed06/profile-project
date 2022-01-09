package com.yan.app.repository;

import com.yan.app.controller.ProfileController;
import com.yan.app.model.UserProfile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserProfileRepositoryTest {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    public void whenFindingCustomerById_thenCorrect() throws ParseException {
        UserProfile userProfile = ProfileController.mockProfile();
        userProfileRepository.save(userProfile);

    }


}