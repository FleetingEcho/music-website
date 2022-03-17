package com.example.yin.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.yin.domain.Collect;
import com.example.yin.service.impl.CollectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("/collection")
public class CollectController {

    @Autowired
    private CollectServiceImpl collectService;

    @ResponseBody
    @PostMapping("/add")
    public JSONObject addCollection(HttpServletRequest req){

        JSONObject resJson = new JSONObject();
        String userId = req.getParameter("userId");
        String type = req.getParameter("type");
        String songId=req.getParameter("songId");
        String song_list_id=req.getParameter("songListId");
        if (Objects.equals(songId, "")){
            resJson.put("code", 0);
            resJson.put("msg", "Empty collection.");
            return resJson;
        } else if (collectService.existSongId(Integer.parseInt(userId), Integer.parseInt(songId))) {
            resJson.put("code", 2);
            resJson.put("msg", "Already collected.");
            return resJson;
        }
        Collect collect = new Collect();
        collect.setUserId(Integer.parseInt(userId));
        collect.setType(Byte.valueOf(type));
        if (Byte.parseByte(type) == 0) {
            collect.setSongId(Integer.parseInt(songId));
        } else if (Byte.parseByte(type) == 1) {
            collect.setSongListId(Integer.parseInt(song_list_id));
        }
        collect.setCreateTime(new Date());
        boolean res = collectService.addCollection(collect);
        if (res){
            resJson.put("code", 1);
            resJson.put("msg", "Successfully collected.");
        }else {
            resJson.put("code", 0);
            resJson.put("msg", "Failed to collect.");
        }
        return resJson;
    }

//    返回所有用户收藏列表
    @GetMapping("")
    public Object allCollection(){
        return collectService.allCollect();
    }

//    返回的指定用户ID收藏列表
@GetMapping("/detail")
    public Object collectionOfUser(HttpServletRequest req){
        String userId = req.getParameter("userId");
        return collectService.collectionOfUser(Integer.parseInt(userId));
    }

//    删除收藏的歌曲
@GetMapping("/delete")
    public Object deleteCollection(HttpServletRequest req){
        String userId = req.getParameter("userId").trim();
        String songId = req.getParameter("songId").trim();
        return collectService.deleteCollect(Integer.parseInt(userId), Integer.parseInt(songId));
    }

//    更新收藏
    @ResponseBody
    @PostMapping(value = "/update")
    public JSONObject updateCollectMsg(HttpServletRequest req){
        JSONObject resJson = new JSONObject();
        String id = req.getParameter("id").trim();
        String userId = req.getParameter("userId").trim();
        String type = req.getParameter("type").trim();
        String songId = req.getParameter("songId").trim();

        Collect collect = new Collect();
        collect.setId(Integer.parseInt(id));
        collect.setUserId(Integer.parseInt(userId));
        collect.setType(Byte.valueOf(type));
        collect.setSongId(Integer.parseInt(songId));

        boolean res = collectService.updateCollectMsg(collect);
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

