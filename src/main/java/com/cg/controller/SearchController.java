//package com.cg.controller;
//
//import com.cg.model.User;
//import com.cg.service.user.IUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/select/ListUser")
//public class SearchController {
//
//    @Autowired
//    private IUserService userService;
//
//    @GetMapping("/search")
//    @ResponseBody
//    public String search(@RequestParam String keyword2){
////        ModelAndView modelAndView = new ModelAndView();
////        modelAndView.setViewName("/user/ListUser");
//
//        keyword2 = "%" + keyword2 + "%";
//
//        System.out.println(keyword2);
//
//        List<User> users = userService.searchAllTemp(keyword2);
//
//        System.out.println(users.toString());
//
//        return users.toString();
//    }
//
//}
