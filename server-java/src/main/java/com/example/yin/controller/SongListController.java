package com.example.yin.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.yin.domain.SongList;
import com.example.yin.service.impl.SongListServiceImpl;
import com.example.yin.constant.Constants;
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


@RestController
@Controller
public class SongListController {

    @Autowired
    private SongListServiceImpl songListService;

    @Configuration
    public static class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) { // windos
                registry.addResourceHandler("/img/songListPic/**")
                        .addResourceLocations("file:" + Constants.RESOURCE_WIN_PATH + "\\img\\songListPic\\");
            } else { // MAC、Linux系统
                registry.addResourceHandler("/img/songListPic/**")
                        .addResourceLocations("file:" + Constants.RESOURCE_MAC_PATH + "/img/songListPic/");
            }
        }
    }

//    添加歌单
    @ResponseBody
    @PostMapping(value = "/songList/add")
    public Object addSongList(HttpServletRequest req){
        JSONObject resJson = new JSONObject();
        String title = req.getParameter("title").trim();
        String pic = req.getParameter("pic").trim();
        String introduction = req.getParameter("introduction").trim();
        String style = req.getParameter("style").trim();

        SongList songList = new SongList();
        songList.setTitle(title);
        songList.setPic(pic);
        songList.setIntroduction(introduction);
        songList.setStyle(style);

        boolean res = songListService.addSongList(songList);
        if (res){
            resJson.put("code", 1);
            resJson.put("msg", "Successfully added.");
        }else {
            resJson.put("code", 0);
            resJson.put("msg", "Failed to add.");
        }
        return resJson;
    }

//    返回所有歌单
    @GetMapping(value = "/songList")
    public Object allSongList(){
        return songListService.allSongList();
    }

//    返回指定标题对应的歌单
    @GetMapping(value = "/songList/title/detail")
    public Object songListOfTitle(HttpServletRequest req){
        String title = req.getParameter("title").trim();
        return songListService.songListOfTitle(title);
    }

//    返回标题包含文字的歌单
    @GetMapping(value = "/songList/likeTitle/detail")
    public Object songListOfLikeTitle(HttpServletRequest req){
        String title = req.getParameter("title").trim();
        return songListService.likeTitle('%'+ title + '%');
    }

//    返回指定类型的歌单
    @GetMapping(value = "/songList/style/detail")
    public Object songListOfStyle(HttpServletRequest req){
        String style = req.getParameter("style").trim();
        return songListService.likeStyle('%'+ style + '%');
    }

//    删除歌单
    @GetMapping(value = "/songList/delete")
    public Object deleteSongList(HttpServletRequest req){
        String id = req.getParameter("id");
        return songListService.deleteSongList(Integer.parseInt(id));
    }

//    更新歌单信息
    @ResponseBody
    @PostMapping(value = "/songList/update")
    public Object updateSongListMsg(HttpServletRequest req){
        JSONObject resJson = new JSONObject();
        String id = req.getParameter("id").trim();
        String title = req.getParameter("title").trim();
        String pic = req.getParameter("pic").trim();
        String introduction = req.getParameter("introduction").trim();
        String style = req.getParameter("style").trim();

        SongList songList = new SongList();
        songList.setId(Integer.parseInt(id));
        songList.setTitle(title);
        songList.setPic(pic);
        songList.setIntroduction(introduction);
        songList.setStyle(style);

        boolean res = songListService.updateSongListMsg(songList);
        if (res){
            resJson.put("code", 1);
            resJson.put("msg", "successfully modified!");
        }else {
            resJson.put("code", 0);
            resJson.put("msg", "Failed to modify.");
        }
        return resJson;
    }

//    更新歌单图片
    @ResponseBody
    @PostMapping(value = "/songList/img/update")
    public Object updateSongListPic(@RequestParam("file") MultipartFile avatarFile, @RequestParam("id")int id){
        JSONObject resJson = new JSONObject();

        if (avatarFile.isEmpty()) {
            resJson.put("code", 0);
            resJson.put("msg", "Failed to upload this file.！");
            return resJson;
        }
        String fileName = System.currentTimeMillis()+avatarFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "songListPic" ;
        File file1 = new File(filePath);
        if (!file1.exists()){
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeAvatorPath = "/img/songListPic/"+fileName;
        try {
            avatarFile.transferTo(dest);
            SongList songList = new SongList();
            songList.setId(id);
            songList.setPic(storeAvatorPath);
            boolean res = songListService.updateSongListImg(songList);
            return getObject(resJson, storeAvatorPath, res);
        }catch (IOException e){
            resJson.put("code", 0);
            resJson.put("msg", "Failed to upload." + e.getMessage());
            return resJson;
        }
    }

    static Object getObject(JSONObject resJson, String storeAvatorPath, boolean res) {
        if (res){
            resJson.put("code", 1);
            resJson.put("avatar", storeAvatorPath);
            resJson.put("msg", "Successfully uploaded.");
        }else {
            resJson.put("code", 0);
            resJson.put("msg", "Failed to upload.");
        }
        return resJson;
    }
}















