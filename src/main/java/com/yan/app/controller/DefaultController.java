package com.yan.app.controller;

import com.yan.app.model.Constants;
import com.yan.app.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DefaultController {

    private final ServletContext mContext;

    @GetMapping("/")
    public ModelAndView main() {
        ModelAndView mv = new ModelAndView();
       // mv.addObject(Constants.KEY_ACTIVE_TAB, Constants.TAB_HOME);
       // mv.addObject(Constants.KEY_MESSAGE, "Home Page");
      //  mv.setViewName("main");
        return new ModelAndView(new RedirectView(mContext.getContextPath() + "/profile/list"));

    }

    @GetMapping("/register")
    public ModelAndView register() {
        ModelAndView mv = new ModelAndView();
        User user = new User();
        user.setEmail("er@rt.com");
        mv.addObject("user", user);
        mv.setViewName("register");
        return mv;
    }

}
