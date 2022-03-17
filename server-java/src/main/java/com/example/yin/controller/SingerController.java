package com.example.yin.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.yin.constant.Constants;
import com.example.yin.domain.Singer;
import com.example.yin.service.impl.SingerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@Controller
public class SingerController {

    @Autowired
    private SingerServiceImpl singerService;

    @Configuration
    public static class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) { // windos系统
                registry.addResourceHandler("/img/singerPic/**")
                        .addResourceLocations("file:" + Constants.RESOURCE_WIN_PATH + "\\img\\singerPic\\");
            } else { // MAC、Linux系统
                registry.addResourceHandler("/img/singerPic/**")
                        .addResourceLocations("file:" + Constants.RESOURCE_MAC_PATH + "/img/singerPic/");
            }
        }
    }

//    添加歌手
    @ResponseBody
    @PostMapping(value = "/singer/add")
    public JSONObject addSinger(HttpServletRequest req){
        JSONObject resJson = new JSONObject();
        String name = req.getParameter("name").trim();
        String sex = req.getParameter("sex").trim();
        String pic = req.getParameter("pic").trim();
        String birth = req.getParameter("birth").trim();
        String location = req.getParameter("location").trim();
        String introduction = req.getParameter("introduction").trim();

        Singer singer = new Singer();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        }catch (Exception e){
            e.printStackTrace();
        }
        singer.setName(name);
        singer.setSex(Byte.valueOf(sex));
        singer.setPic(pic);
        singer.setBirth(myBirth);
        singer.setLocation(location);
        singer.setIntroduction(introduction);

        boolean res = singerService.addSinger(singer);
        if (res){
            resJson.put("code", 1);
            resJson.put("msg", "Successfully added.");
        }else {
            resJson.put("code", 0);
            resJson.put("msg", "Failed to add.");
        }
        return resJson;
    }

//    返回所有歌手
    @GetMapping(value = "/singer")
    public Object allSinger(){
        return singerService.allSinger();
    }

//    根据歌手名查找歌手
    @GetMapping(value = "/singer/name/detail")
    public Object singerOfName(HttpServletRequest req){
        String name = req.getParameter("name").trim();
        return singerService.singerOfName(name);
    }

//    根据歌手性别查找歌手
    @GetMapping(value = "/singer/sex/detail")
    public Object singerOfSex(HttpServletRequest req){
        String sex = req.getParameter("sex").trim();
        return singerService.singerOfSex(Integer.parseInt(sex));
    }

//    删除歌手
    @GetMapping(value = "/singer/delete")
    public Object deleteSinger(HttpServletRequest req){
        String id = req.getParameter("id");
        return singerService.deleteSinger(Integer.parseInt(id));
    }

//    更新歌手信息
    @ResponseBody
    @PostMapping(value = "/singer/update")
    public JSONObject updateSingerMsg(HttpServletRequest req){
        JSONObject resJson = new JSONObject();
        String id = req.getParameter("id").trim();
        String name = req.getParameter("name").trim();
        String sex = req.getParameter("sex").trim();
        String pic = req.getParameter("pic").trim();
        String birth = req.getParameter("birth").trim();
        String location = req.getParameter("location").trim();
        String introduction = req.getParameter("introduction").trim();

        Singer singer = new Singer();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        }catch (Exception e){
            e.printStackTrace();
        }
        singer.setId(Integer.parseInt(id));
        singer.setName(name);
        singer.setSex(Byte.valueOf(sex));
        singer.setPic(pic);
        singer.setBirth(myBirth);
        singer.setLocation(location);
        singer.setIntroduction(introduction);

        boolean res = singerService.updateSingerMsg(singer);
        if (res){
            resJson.put("code", 1);
            resJson.put("msg", "successfully modified!");
        }else {
            resJson.put("code", 0);
            resJson.put("msg", "Failed to modify.");
        }
        return resJson;
    }

//    更新歌手头像
    @ResponseBody
    @PostMapping(value = "/singer/avatar/update")
    public JSONObject updateSingerPic(@RequestParam("file") MultipartFile avatarFile, @RequestParam("id")int id){
        JSONObject resJson = new JSONObject();

        if (avatarFile.isEmpty()) {
            resJson.put("code", 0);
            resJson.put("msg", "Failed to upload this file.！");
            return resJson;
        }
        String fileName = System.currentTimeMillis()+avatarFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "singerPic" ;
        File file1 = new File(filePath);
        if (!file1.exists()){
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeAvatorPath = "/img/singerPic/"+fileName;
        try {
            avatarFile.transferTo(dest);
            Singer singer = new Singer();
            singer.setId(id);
            singer.setPic(storeAvatorPath);
            boolean res = singerService.updateSingerPic(singer);
            if (res){
                resJson.put("code", 1);
                resJson.put("pic", storeAvatorPath);
                resJson.put("msg", "Successfully uploaded.");
            }else {
                resJson.put("code", 0);
                resJson.put("msg", "Failed to upload.");
            }
            return resJson;
        }catch (IOException e){
            resJson.put("code", 0);
            resJson.put("msg", "Failed to upload." + e.getMessage());
            return resJson;
        }
    }
}

