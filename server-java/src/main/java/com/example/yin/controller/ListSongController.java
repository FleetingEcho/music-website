package com.example.yin.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.yin.domain.ListSong;
import com.example.yin.service.impl.ListSongServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Controller
public class ListSongController {

    @Autowired
    private ListSongServiceImpl listSongService;

//    给歌单添加歌曲
    @ResponseBody
    @PostMapping( "/ListSong/add")
    public JSONObject addListSong(HttpServletRequest req){
    JSONObject resJson = new JSONObject();
    String songId = req.getParameter("songId").trim();
    String songListId = req.getParameter("songListId").trim();

    ListSong listsong = new ListSong();
    listsong.setSongId(Integer.parseInt(songId));
    listsong.setSongListId(Integer.parseInt(songListId));

    boolean res = listSongService.addListSong(listsong);
    if (res){
        resJson.put("code", 1);
        resJson.put("msg", "Successfully added.");
    }else {
        resJson.put("code", 0);
        resJson.put("msg", "Failed to add.");
    }
        return resJson;
    }

//    返回歌单里包含的所有歌曲
    @GetMapping( "/listSong")
    public Object allListSong(){
        return listSongService.allListSong();
    }

//    返回歌单里指定歌单ID的歌曲
    @GetMapping( "/listSong/detail")
    public Object listSongOfSongId(HttpServletRequest req){
        String songListId = req.getParameter("songListId");
        return listSongService.listSongOfSongId(Integer.parseInt(songListId));
    }

//    删除歌单里的歌曲
    @GetMapping( "/ListSong/delete")
    public Object deleteListSong(HttpServletRequest req){
        String songId = req.getParameter("songId");
        return listSongService.deleteListSong(Integer.parseInt(songId));
    }

//    更新歌单里面的歌曲信息
    @ResponseBody
    @PostMapping( "/listSong/update")
    public JSONObject updateListSongMsg(HttpServletRequest req){
        JSONObject resJson = new JSONObject();
        String id = req.getParameter("id").trim();
        String songId = req.getParameter("songId").trim();
        String songListId = req.getParameter("songListId").trim();

        ListSong listsong = new ListSong();
        listsong.setId(Integer.parseInt(id));
        listsong.setSongId(Integer.parseInt(songId));
        listsong.setSongListId(Integer.parseInt(songListId));

        boolean res = listSongService.updateListSongMsg(listsong);
        if (res){
            resJson.put("code", 1);
            resJson.put("msg", "successfully modified!");
        }else {
            resJson.put("code", 0);
            resJson.put("msg", "Failed to modify.");
        }
        return resJson;
    }
}
