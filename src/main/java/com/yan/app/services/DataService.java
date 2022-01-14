package com.yan.app.services;

import com.yan.app.model.UserProfile;
import com.yan.app.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataService {
    private final UserProfileRepository userProfileRepository;

    public void addUserProfile(UserProfile userProfile) {

    }

    public void updateUserProfile(UserProfile userProfile) {

    }

    public UserProfile getUserProfileByName(String name) {
        return null;
    }

    @Transactional
    public void saveImageFile(UUID profileUuid, MultipartFile file) {

        try {
            UserProfile userProfile = userProfileRepository.findByUuid(profileUuid);

            Byte[] byteObjects = new Byte[file.getBytes().length];

            int i = 0;

            for (byte b : file.getBytes()){
                byteObjects[i++] = b;
            }

            userProfile.setImage(byteObjects);

            userProfileRepository.save(userProfile);
        } catch (IOException e) {
            log.error("Error occurred", e);
        }
    }

}
