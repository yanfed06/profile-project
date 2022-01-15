package com.yan.app.services;

import com.yan.app.model.UserProfile;
import com.yan.app.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import static java.nio.file.StandardOpenOption.*;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            Path userDirectory = Paths.get("")
                    .toAbsolutePath();
            Path dir = userDirectory.resolve("files");
            if(!Files.exists(dir)){
                Files.createDirectory(dir);

            }

            String fileName = "file_" + System.nanoTime() + ".jpg";

            Path outputPath = dir.resolve(fileName);
            Files.write(outputPath, file.getBytes(), CREATE_NEW);
            userProfile.setImageName(outputPath.toString());

            userProfileRepository.save(userProfile);
        } catch (IOException e) {
            log.error("Error occurred", e);
        }
    }

}
