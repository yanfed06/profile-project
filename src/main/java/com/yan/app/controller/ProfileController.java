package com.yan.app.controller;

import com.yan.app.model.Achievement;
import com.yan.app.model.Constants;
import com.yan.app.model.UserProfile;
import com.yan.app.repository.AchievementRepository;
import com.yan.app.repository.UserProfileRepository;
import com.yan.app.services.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/profile")
@Slf4j
public class ProfileController {

    private final UserProfileRepository userProfileRepository;
    private final AchievementRepository achievementRepository;
    private final ServletContext mContext;
    private final DataService dataService;
    @Autowired
    ResourceLoader resourceLoader;

    @GetMapping("/list")
    public ModelAndView getProfileList() {
        ModelAndView mv = new ModelAndView();
        mv.addObject(Constants.KEY_ACTIVE_TAB, Constants.TAB_LIST);
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        mv.addObject("userProfileList", userProfiles);
        mv.addObject(Constants.SELECTED, "");
        mv.setViewName("main");
        return mv;
    }

    @GetMapping("/new")
    public ModelAndView profileNew() throws ParseException {
        UserProfile userProfile = UserProfile.builder()
                .uuid(UUID.randomUUID())
                .build();
        ModelAndView mv = new ModelAndView();
        mv.addObject(Constants.KEY_ACTIVE_TAB, Constants.TAB_EDIT);
        mv.addObject(Constants.KEY_USER_PROFILE, userProfile);
        mv.addObject(Constants.SELECTED, userProfile.getUuid());
        mv.setViewName("main");
        return mv;
    }

    @GetMapping(value = {"/view", "/view/{uuid}"})
    public ModelAndView profileView(@PathVariable(required = false) UUID uuid) throws ParseException {
        ModelAndView mv = new ModelAndView();
        mv.addObject(Constants.KEY_ACTIVE_TAB, Constants.TAB_VIEW);
        UserProfile userProfile;
        if(uuid == null) {
            List<UserProfile> userProfiles = userProfileRepository.findAll();
            log.info("profiles: {}", userProfiles);
            userProfile = userProfiles.get(0);
        } else
            userProfile = userProfileRepository.findByUuid(uuid);
        mv.addObject(Constants.KEY_USER_PROFILE, userProfile);
        mv.addObject(Constants.SELECTED, userProfile.getUuid());
        mv.setViewName("main");
        return mv;
    }

    @GetMapping(value = {"/edit", "/edit/{uuid}"})
    public ModelAndView profileEdit(@PathVariable( required = false) UUID uuid) throws ParseException {
        UserProfile userProfile;
        if(uuid == null) {
            List<UserProfile> userProfiles = userProfileRepository.findAll();
            log.info("profiles: {}", userProfiles);
            userProfile = userProfiles.get(0);
        } else
            userProfile = userProfileRepository.findByUuid(uuid);
        ModelAndView mv = new ModelAndView();
        mv.addObject(Constants.KEY_ACTIVE_TAB, Constants.TAB_EDIT);
        mv.addObject(Constants.KEY_USER_PROFILE, userProfile);
        mv.addObject(Constants.SELECTED, userProfile.getUuid());
        mv.setViewName("main");
        return mv;
    }
    @PostMapping("/edit")
    public ModelAndView updateProfile(UserProfile userProfile) throws ParseException {
        log.info("Profile: {}", userProfile);
        userProfile = userProfileRepository.save(userProfile);

        ModelAndView mv = new ModelAndView();
        mv.addObject(Constants.KEY_ACTIVE_TAB, Constants.TAB_VIEW);
        mv.addObject(Constants.KEY_USER_PROFILE, userProfile);
        mv.addObject(Constants.SELECTED, userProfile.getUuid());
        mv.setViewName("main");
        return mv;
    }

    @PostMapping("/upload")
    public RedirectView handleFileUpload(@RequestParam("uuid") UUID uuid, @RequestParam("file") MultipartFile file) throws IOException {
        log.info("uuid: {}", uuid);
        dataService.saveImageFile(uuid,  file);
        return new RedirectView(mContext.getContextPath() + "/profile/edit/"+uuid);
    }

    @PostMapping("/achievement/add")
    public RedirectView addAchievement(@RequestParam("uuid") UUID uuid, @RequestParam("file") MultipartFile file,
                                       @RequestParam("description") String description) throws IOException {
        log.info("uuid: {}", uuid);
        dataService.addAchievement(uuid,  file, description);
        return new RedirectView(mContext.getContextPath() + "/profile/edit/"+uuid);
    }

    @GetMapping("/achievement/delete/{uuid}")
    public RedirectView deleteAchievement(@PathVariable("uuid") UUID uuid) throws IOException {
        log.info("uuid: {}", uuid);
        Achievement achievement = achievementRepository.findByUuid(uuid);
        Files.deleteIfExists(Paths.get(achievement.getImageName()));
        UUID profileUuid = achievement.getUserProfile().getUuid();
        achievementRepository.delete(achievement);
        return new RedirectView(mContext.getContextPath() + "/profile/edit/"+profileUuid);
    }

    @RequestMapping(value = "/image/{uuid}", method = RequestMethod.GET)
    public void getImageAsByteArray(@PathVariable("uuid") String uuid, HttpServletResponse response) throws IOException {
        UserProfile userProfile = userProfileRepository.findByUuid(UUID.fromString(uuid));
        InputStream in;
        if(userProfile == null || userProfile.getImageName() == null) {
             //in = mContext.getResourceAsStream("classpath:static/images/1.jpg");
            Resource resource = resourceLoader.getResource("classpath:static/images/1.jpg");
            in = resource.getInputStream();
        } else
             in = new FileInputStream(new File(userProfile.getImageName()));
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

    @RequestMapping(value = "/imageach/{uuid}", method = RequestMethod.GET)
    public void getImageAchAsByteArray(@PathVariable("uuid") String uuid, HttpServletResponse response) throws IOException {
        Achievement achievement = achievementRepository.findByUuid(UUID.fromString(uuid));
        InputStream in;
        if(achievement == null || achievement.getImageName() == null) {
            //in = mContext.getResourceAsStream("classpath:static/images/1.jpg");
            Resource resource = resourceLoader.getResource("classpath:static/images/1.jpg");
            in = resource.getInputStream();
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        } else {

            String ext = achievement.getImageName().substring(achievement.getImageName().lastIndexOf(".")+1);
            if(ext.equalsIgnoreCase("pdf")) {
                Resource resource = resourceLoader.getResource("classpath:static/images/pdf.png");
                in = resource.getInputStream();
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
            }
            else {
                in = new FileInputStream(new File(achievement.getImageName()));
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            }
        }

        IOUtils.copy(in, response.getOutputStream());
    }

    public static UserProfile mockProfile() throws ParseException {
        return UserProfile.builder()
                .uuid(UUID.randomUUID())
                .firstName("Yan")
                .lastName("Fedorov")
                .hobbies("computer games")
                .achievements("-")
                .creation("-")
                .school("?????????? ???144")
                .birthday(getDateFromString("2006-03-05"))
                .homeland("Russia")
                .schoolLife("--")
                .studies("--")
                .build();
    }

    public static  Date getDateFromString(final String date) throws ParseException {
        SimpleDateFormat simpleDateFormatTarget = new SimpleDateFormat("yyyy-MM-dd",
                Locale.ENGLISH);
        return simpleDateFormatTarget.parse(date);
    }

    //  @PostConstruct
    @Transactional
    public void initPost() throws ParseException {
        if(userProfileRepository.findByFirstName("Yan") == null) {
            UserProfile userProfile = ProfileController.mockProfile();
            userProfileRepository.save(userProfile);
        }
    }
}
