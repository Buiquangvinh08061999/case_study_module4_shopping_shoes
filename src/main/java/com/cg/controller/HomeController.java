package com.cg.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/select")
public class HomeController {

    @GetMapping("/homepage")
    public ModelAndView showPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/homedashboard/homepage");
        return modelAndView;
    }

    @GetMapping("/ListUser")
    public ModelAndView showListUser(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/user/ListUser");
        return modelAndView;
    }


    @GetMapping("/ListProduct")
    public ModelAndView showListProduct(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/product/ListProduct");
        return modelAndView;
    }

}
