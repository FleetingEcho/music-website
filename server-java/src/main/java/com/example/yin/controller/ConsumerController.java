package com.example.yin.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.yin.constant.Constants;
import com.example.yin.domain.Consumer;
import com.example.yin.service.impl.ConsumerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@Controller
public class ConsumerController {

    @Autowired
    private ConsumerServiceImpl consumerService;

    @EnableWebMvc
    @Configuration
    public static class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) {
                registry.addResourceHandler("/img/avatarImages/**")
                        //.addResourceLocations("file:" + Constants.RESOURCE_WIN_PATH + "/img/avatarImages/");
                        .addResourceLocations("file:" + Constants.RESOURCE_WIN_PATH + "\\img\\avatarImages\\");
            } else { // MAC、Linux
                registry.addResourceHandler("/img/avatarImages/**")
                        .addResourceLocations("file:" + Constants.RESOURCE_MAC_PATH + "/img/avatarImages/");
            }
        }
    }

//    添加用户
    @ResponseBody
    @PostMapping(value = "/user/add")
    public JSONObject addUser(HttpServletRequest req){
        JSONObject resJson = new JSONObject();
        String username = req.getParameter("username").trim();
        String password = req.getParameter("password").trim();
        String sex = req.getParameter("sex").trim();
        String phoneNum = req.getParameter("phone_num").trim();
        String email = req.getParameter("email").trim();
        String birth = req.getParameter("birth").trim();
        String introduction = req.getParameter("introduction").trim();
        String location = req.getParameter("location").trim();
        String avatar = req.getParameter("avatar").trim();

        if ("".equals(username)){
            resJson.put("code", 0);
            resJson.put("msg", "Error in username or password.");
            return resJson;
        }
        Consumer consumer = new Consumer();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        } catch (Exception e){
            e.printStackTrace();
        }
        consumer.setUsername(username);
        consumer.setPassword(password);
        consumer.setSex(Byte.valueOf(sex));
        if ("".equals(phoneNum)) {
            consumer.setPhoneNum(null);
        } else{
            consumer.setPhoneNum(phoneNum);
        }

        if ("".equals(email)) {
            consumer.setEmail(null);
        } else{
            consumer.setEmail(email);
        }
        consumer.setBirth(myBirth);
        consumer.setIntroduction(introduction);
        consumer.setLocation(location);
        consumer.setAvatar(avatar);
        consumer.setCreateTime(new Date());
        consumer.setUpdateTime(new Date());

        boolean res = consumerService.addUser(consumer);
        if (res) {
            resJson.put("code", 1);
            resJson.put("msg", "Successfully registered.");
        } else {
            resJson.put("code", 0);
            resJson.put("msg", "Failed to register.");
        }
        return resJson;
    }

//    判断是否Successfully logged in.
    @ResponseBody
    @PostMapping(value = "/user/login/status")
    public JSONObject loginStatus(@RequestParam(name = "username", defaultValue = "Jake") String username,@RequestParam(name = "password", defaultValue = "123") String password, HttpSession session){
        JSONObject resJson = new JSONObject();
        System.out.println(username);
        System.out.println(password);
        boolean res = consumerService.verifyPassword(username, password);
        if (res){
            resJson.put("code", 1);
            resJson.put("msg", "Successfully logged in.");
            resJson.put("userMsg", consumerService.loginStatus(username));
            session.setAttribute("username", username);
        }else {
            resJson.put("code", 0);
            resJson.put("msg", "Error in username or password.");
        }
        return resJson;

    }

//    返回所有用户
    @GetMapping(value = "/user")
    public Object allUser(){
        return consumerService.allUser();
    }

//    返回指定ID的用户
    @GetMapping(value = "/user/detail")
    public Object userOfId(HttpServletRequest req){
        String id = req.getParameter("id");
        return consumerService.userOfId(Integer.parseInt(id));
    }

//    删除用户
    @GetMapping(value = "/user/delete")
    public Object deleteUser(HttpServletRequest req){
        String id = req.getParameter("id");
        return consumerService.deleteUser(Integer.parseInt(id));
    }

//    更新用户信息
    @ResponseBody
    @PostMapping(value = "/user/update")
    public JSONObject updateUserMsg(HttpServletRequest req){
        JSONObject resJson = new JSONObject();
        String id = req.getParameter("id").trim();
        String username = req.getParameter("username").trim();
        String password = req.getParameter("password").trim();
        String sex = req.getParameter("sex").trim();
        String phoneNum = req.getParameter("phone_num").trim();
        String email = req.getParameter("email").trim();
        String birth = req.getParameter("birth").trim();
        String introduction = req.getParameter("introduction").trim();
        String location = req.getParameter("location").trim();

        if ("".equals(username)){
            resJson.put("code", 0);
            resJson.put("msg", "Error in username or password.");
            return resJson;
        }
        Consumer consumer = new Consumer();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        }catch (Exception e){
            e.printStackTrace();
        }
        consumer.setId(Integer.parseInt(id));
        consumer.setUsername(username);
        consumer.setPassword(password);
        consumer.setSex(Byte.valueOf(sex));
        consumer.setPhoneNum(phoneNum);
        consumer.setEmail(email);
        consumer.setBirth(myBirth);
        consumer.setIntroduction(introduction);
        consumer.setLocation(location);
        consumer.setUpdateTime(new Date());

        boolean res = consumerService.updateUserMsg(consumer);
        if (res){
            resJson.put("code", 1);
            resJson.put("msg", "Successfully modified!");
            return resJson;
        }else {
            resJson.put("code", 0);
            resJson.put("msg", "Failed to modify.");
            return resJson;
        }
    }

//    更新用户头像
    @ResponseBody
    @PostMapping(value = "/user/avatar/update")
    public JSONObject updateUserPic(@RequestParam("file") MultipartFile avatarFile, @RequestParam("id")int id){
        JSONObject resJson = new JSONObject();

        if (avatarFile.isEmpty()) {
            resJson.put("code", 0);
            resJson.put("msg", "Failed to upload this file.！");
            return resJson;
        }
        String fileName = System.currentTimeMillis()+avatarFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "avatarImages" ;
        File file1 = new File(filePath);
        if (!file1.exists()){
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeAvatorPath = "/img/avatarImages/"+fileName;
        try {
            avatarFile.transferTo(dest);
            Consumer consumer = new Consumer();
            consumer.setId(id);
            consumer.setAvatar(storeAvatorPath);
            boolean res = consumerService.updateUserAvator(consumer);
            if (res){
                resJson.put("code", 1);
                resJson.put("avatar", storeAvatorPath);
                resJson.put("msg", "Successfully uploaded!");
            }else {
                resJson.put("code", 0);
                resJson.put("msg", "Failed to upload.");
            }
        }catch (IOException e){
            resJson.put("code", 0);
            resJson.put("msg", "Failed to upload."+e.getMessage());
            return resJson;
        }
        return resJson;
    }
}
