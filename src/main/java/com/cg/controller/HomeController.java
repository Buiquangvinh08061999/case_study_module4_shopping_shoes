package com.cg.controller;


import com.cg.model.dto.UserDTO;
import com.cg.service.product.IProductService;
import com.cg.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/select")
public class HomeController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IProductService productService;

    @GetMapping("/homepage")
    public ModelAndView showPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/homedashboard/homepage");
        modelAndView.addObject("userDTO", getUserDTO());
        return modelAndView;
    }


    @GetMapping("/ListUser")
    public ModelAndView showListUser(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/user/ListUser");
        modelAndView.addObject("userDTO", getUserDTO());
        return modelAndView;
    }
    /*hiển thị trang history product*/
    @GetMapping("/ListHistoryUser")
    public ModelAndView showListUserHistory(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/user/history_deleted_user");
        modelAndView.addObject("userDTO", getUserDTO());
        return modelAndView;
    }


    @GetMapping("/ListProduct")
    public ModelAndView showListProduct(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/product/ListProduct");
        modelAndView.addObject("userDTO", getUserDTO());
        return modelAndView;
    }

    /*hiển thị trang history product*/
    @GetMapping("/ListHistoryProduct")
    public ModelAndView showListProductHistory(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/product/history_deleted_product");
        modelAndView.addObject("userDTO", getUserDTO());

        return modelAndView;
    }

    /*hàm này kết hợp để tìm ra tất cả các trường trong bản user(đặt biệt ta lấy Id và fullname của nó, để sử dụng để triển khai phương thức,(rất quan trọng) */
    private String getPrincipal(){
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof UserDetails){
            username = ((UserDetails) principal).getUsername();
        }else {
            username ="";
        }
        return username;
    }

    /**
     * Ở đây viết gì, thì khi gọi lại bất cứ đâu, rê vào sẽ hiển thị nội dung này
     * @return
     */
    private UserDTO getUserDTO(){
        String username = getPrincipal();

        Optional<UserDTO> userDTOOptional = userService.findUserDTOByUsername(username);
        if(!userDTOOptional.isPresent()){
            return null;
        }
        return userDTOOptional.get();
    }

    /*khi qua trang order vần hiển thị tên người dùng và id người dùng bên trang này*, trang cho nguoi dung*/
    @GetMapping("/ListOrder")
    public ModelAndView showListOrder(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/order/ListOrder");
        modelAndView.addObject("userDTO", getUserDTO());
        return modelAndView;
    }


    /*Trang admin quản lý, cha*/
    @GetMapping("/ListOrderDashBoard")
    public ModelAndView showListOrderDashBoard(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/orderDashBoard/ListOrderDashBoard");
        modelAndView.addObject("userDTO", getUserDTO());
        return modelAndView;
    }



    /*Hiển thị ở phần giỏ hàng tên người dùng và id người dùng ở giỏ này để xử lí*/
    @GetMapping("/ListCart")
    public ModelAndView showListCart(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/order/Cart");
        modelAndView.addObject("userDTO", getUserDTO());

        return modelAndView;
    }







}
