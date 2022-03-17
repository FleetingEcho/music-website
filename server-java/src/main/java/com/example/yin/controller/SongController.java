package com.example.yin.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.yin.domain.Song;
import com.example.yin.service.impl.SongServiceImpl;
import com.example.yin.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@RestController
@Controller
public class SongController {

    @Autowired
    private SongServiceImpl songService;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.of(10, DataUnit.MEGABYTES));
        factory.setMaxRequestSize(DataSize.of(10, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }

    @EnableWebMvc
    @Configuration
    public static class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) { // windos系统
                registry.addResourceHandler("/img/songPic/**")
                        .addResourceLocations("file:" + Constants.RESOURCE_WIN_PATH + "\\img\\songPic\\");
                registry.addResourceHandler("/song/**")
                        //.addResourceLocations("file:" + Constants.RESOURCE_WIN_PATH + "\\song\\");
                        .addResourceLocations("file:" + Constants.RESOURCE_WIN_PATH + "\\song\\");
            } else { // MAC、Linux系统
                registry.addResourceHandler("/img/songPic/**")
                        .addResourceLocations("file:" + Constants.RESOURCE_MAC_PATH + "/img/songPic/");
                registry.addResourceHandler("/song/**")
                        .addResourceLocations("file:" + Constants.RESOURCE_MAC_PATH + "/song/");
            }
        }
    }

//    添加歌曲
    @ResponseBody
    @PostMapping(value = "/song/add")
    public Object addSong(HttpServletRequest req, @RequestParam("file") MultipartFile mpfile){
        JSONObject resJson = new JSONObject();
        String singer_id = req.getParameter("singerId").trim();
        String name = req.getParameter("name").trim();
        String introduction = req.getParameter("introduction").trim();
        String pic = "/img/songPic/tubiao.jpg";
        String lyric = req.getParameter("lyric").trim();

        if (mpfile.isEmpty()) {
            resJson.put("code", 0);
            resJson.put("msg", "Music failed to upload.！");
            return resJson;
        }
        String fileName = mpfile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "song";
        File file1 = new File(filePath);
        if (!file1.exists()){
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/song/"+fileName;
        try {
            mpfile.transferTo(dest);
            Song song = new Song();
            song.setSingerId(Integer.parseInt(singer_id));
            song.setName(name);
            song.setIntroduction(introduction);
            song.setCreateTime(new Date());
            song.setUpdateTime(new Date());
            song.setPic(pic);
            song.setLyric(lyric);
            song.setUrl(storeUrlPath);
            boolean res = songService.addSong(song);
            return getObject(resJson, storeUrlPath, res);
        } catch (IOException e) {
            resJson.put("code", 0);
            resJson.put("msg", "Failed to upload." + e.getMessage());
            return resJson;
        }
    }

//    返回所有歌曲
    @GetMapping(value = "/song")
    public Object allSong(){
        return songService.allSong();
    }

//    返回指定歌曲ID的歌曲
    @GetMapping(value = "/song/detail")
    public Object songOfId(HttpServletRequest req){
        String id = req.getParameter("id");
        return songService.songOfId(Integer.parseInt(id));
    }

//    返回指定歌手ID的歌曲
    @GetMapping(value = "/song/singer/detail")
    public Object songOfSingerId(HttpServletRequest req){
        String singerId = req.getParameter("singerId");
        return songService.songOfSingerId(Integer.parseInt(singerId));
    }

//    返回指定歌手名的歌曲
    @GetMapping(value = "/song/singerName/detail")
    public Object songOfSingerName(HttpServletRequest req){
        String name = req.getParameter("name");
        return songService.songOfSingerName('%'+ name + '%');
    }

//    返回指定歌曲名的歌曲
    @GetMapping(value = "/song/name/detail")
    public Object songOfName(HttpServletRequest req){
        String name = req.getParameter("name").trim();
        return songService.songOfName(name);
    }

//    删除歌曲
    @GetMapping(value = "/song/delete")
    public Object deleteSong(HttpServletRequest req){
        String id = req.getParameter("id");
        return songService.deleteSong(Integer.parseInt(id));
    }

//    更新歌曲信息
    @ResponseBody
    @PostMapping(value = "/song/update")
    public Object updateSongMsg(HttpServletRequest req){
        JSONObject resJson = new JSONObject();
        String id = req.getParameter("id").trim();
        String singer_id = req.getParameter("singerId").trim();
        String name = req.getParameter("name").trim();
        String introduction = req.getParameter("introduction").trim();
        String lyric = req.getParameter("lyric").trim();

        Song song = new Song();
        song.setId(Integer.parseInt(id));
        song.setSingerId(Integer.parseInt(singer_id));
        song.setName(name);
        song.setIntroduction(introduction);
        song.setUpdateTime(new Date());
        song.setLyric(lyric);

        boolean res = songService.updateSongMsg(song);
        if (res){
            resJson.put("code", 1);
            resJson.put("msg", "successfully modified!");
        }else {
            resJson.put("code", 0);
            resJson.put("msg", "Failed to modify.");
        }
        return resJson;
    }

//    更新歌曲图片
    @ResponseBody
    @PostMapping(value = "/song/img/update")
    public Object updateSongPic(@RequestParam("file") MultipartFile urlFile, @RequestParam("id")int id){
        JSONObject resJson = new JSONObject();

        if (urlFile.isEmpty()) {
            resJson.put("code", 0);
            resJson.put("msg", "Music failed to upload.！");
            return resJson;
        }
        String fileName = System.currentTimeMillis()+urlFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "songPic";
        File file1 = new File(filePath);
        if (!file1.exists()){
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/img/songPic/"+fileName;
        try {
            urlFile.transferTo(dest);
            Song song = new Song();
            song.setId(id);
            song.setPic(storeUrlPath);
            boolean res = songService.updateSongPic(song);
            return getObject(resJson, storeUrlPath, res);
        }catch (IOException e){
            resJson.put("code", 0);
            resJson.put("msg", "Failed to upload." + e.getMessage());
            return resJson;
        }
    }

//    更新歌曲URL
    @ResponseBody
    @PostMapping(value = "/song/url/update")
    public Object updateSongUrl(@RequestParam("file") MultipartFile urlFile, @RequestParam("id")int id){
        JSONObject resJson = new JSONObject();

        if (urlFile.isEmpty()) {
            resJson.put("code", 0);
            resJson.put("msg", "Music failed to upload.！");
            return resJson;
        }
        String fileName = urlFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "song";
        File file1 = new File(filePath);
        if (!file1.exists()){
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/song/"+fileName;
        try {
            urlFile.transferTo(dest);
            Song song = new Song();
            song.setId(id);
            song.setUrl(storeUrlPath);
            boolean res = songService.updateSongUrl(song);
            return getObject(resJson, storeUrlPath, res);
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
