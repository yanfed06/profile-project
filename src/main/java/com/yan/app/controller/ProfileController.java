package com.yan.app.controller;

import com.yan.app.model.Constants;
import com.yan.app.model.UserProfile;
import com.yan.app.repository.UserProfileRepository;
import com.yan.app.services.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.websocket.server.PathParam;
import java.io.IOException;
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
    private final ServletContext mContext;
    private final DataService dataService;



    @GetMapping("/view")
    public ModelAndView profileView() throws ParseException {
        ModelAndView mv = new ModelAndView();
        mv.addObject(Constants.KEY_ACTIVE_TAB, Constants.TAB_VIEW);
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        log.info("profiles: {}", userProfiles);
        UserProfile userProfile = userProfiles.get(0);
        mv.addObject(Constants.KEY_USER_PROFILE, userProfile);
        mv.setViewName("main");
        return mv;
    }

    @GetMapping("/edit")
    public ModelAndView profileEdit() throws ParseException {
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        log.info("profiles: {}", userProfiles);
        UserProfile userProfile = userProfiles.get(0);
        ModelAndView mv = new ModelAndView();
        mv.addObject(Constants.KEY_ACTIVE_TAB, Constants.TAB_EDIT);
        mv.addObject(Constants.KEY_USER_PROFILE, userProfile);
        mv.setViewName("main");
        return mv;
    }
    @PostMapping("/edit")
 //   @Transactional
    public ModelAndView updateProfile(UserProfile userProfile) throws ParseException {
        log.info("Profile: {}", userProfile);
        if(userProfileRepository.existsById(userProfile.getUuid())) {
            userProfile = userProfileRepository.save(userProfile);
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject(Constants.KEY_ACTIVE_TAB, Constants.TAB_VIEW);
        mv.addObject(Constants.KEY_USER_PROFILE, userProfile);
        mv.setViewName("main");
        return mv;
    }

    public static UserProfile mockProfile() throws ParseException {
        return UserProfile.builder()
                .uuid(UUID.randomUUID())
                .firstName("Yan")
                .lastName("Fedorov")
                .hobbies("computer games")
                .achievements("-")
                .creation("-")
                .school("Лицей №144")
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

    @PostConstruct
    @Transactional
    public void initPost() throws ParseException {
        if(userProfileRepository.findByFirstName("Yan") == null) {
            UserProfile userProfile = ProfileController.mockProfile();
            userProfileRepository.save(userProfile);
        }
    }

    @PostMapping("/upload/{uuid}")
    public RedirectView handleFileUpload(@PathParam("uuid") UUID uuid, @RequestParam("file") MultipartFile file) throws IOException {

        dataService.saveImageFile(uuid,  file);
        return new RedirectView(mContext.getContextPath() + "/profile/edit");
    }

}
