package com.yan.app.controller;

import com.yan.app.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DefaultController {

    @GetMapping("/test")
    public ModelAndView test() {
        ModelAndView mv = new ModelAndView();
        User user = new User();
        user.setEmail("er@rt.com");
        mv.addObject("user", user);
        mv.setViewName("index");
        return mv;
    }
}
