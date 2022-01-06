package com.yan.app.controller;

import com.yan.app.model.Constants;
import com.yan.app.model.UserProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/profile")
@Slf4j
public class ProfileController {

    @GetMapping("/view")
    public ModelAndView profileView() throws ParseException {
        ModelAndView mv = new ModelAndView();
        mv.addObject(Constants.KEY_ACTIVE_TAB, Constants.TAB_VIEW);
        mv.addObject(Constants.KEY_USER_PROFILE, mockProfile());
        mv.setViewName("main");
        return mv;
    }

    @GetMapping("/edit")
    public ModelAndView profileEdit() throws ParseException {
        ModelAndView mv = new ModelAndView();
        mv.addObject(Constants.KEY_ACTIVE_TAB, Constants.TAB_EDIT);
        mv.addObject(Constants.KEY_USER_PROFILE, mockProfile());
        mv.setViewName("main");
        return mv;
    }

    private UserProfile mockProfile() throws ParseException {
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

    public  Date getDateFromString(final String date) throws ParseException {
        SimpleDateFormat simpleDateFormatTarget = new SimpleDateFormat("yyyy-MM-dd",
                Locale.ENGLISH);
        return simpleDateFormatTarget.parse(date);
    }
}
