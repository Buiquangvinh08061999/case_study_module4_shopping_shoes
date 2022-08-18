package com.cg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/select")
public class ErrorController {

    @GetMapping("/403")
    public ModelAndView showPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/error/403");
        return modelAndView;
    }
}
