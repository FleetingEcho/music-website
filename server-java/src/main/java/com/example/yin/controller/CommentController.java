package com.example.yin.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.yin.domain.Comment;
import com.example.yin.service.impl.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@Controller
public class CommentController {
    @Autowired
    private CommentServiceImpl commentService;

//  提交评论
    @ResponseBody
    @PostMapping( "/comment/add")
    public JSONObject addComment(HttpServletRequest req){

        JSONObject resJson = new JSONObject();
        String userId = req.getParameter("userId");
        String type = req.getParameter("type");
        String songListId=req.getParameter("songListId");
        String songId=req.getParameter("songId");
        String content = req.getParameter("content").trim();

        Comment comment = new Comment();
        comment.setUserId(Integer.parseInt(userId));
        comment.setType(Byte.valueOf(type));
        if (Byte.parseByte(type) == 0) {
            comment.setSongId(Integer.parseInt(songId));
        } else if (Byte.parseByte(type) == 1) {
            comment.setSongListId(Integer.parseInt(songListId));
        }
        comment.setContent(content);
        comment.setCreateTime(new Date());
        boolean res = commentService.addComment(comment);
        if (res){
            resJson.put("code", 1);
            resJson.put("msg", "Successfully commented.");
        }else {
            resJson.put("code", 0);
            resJson.put("msg", "Failed to add comment.");
        }
        return resJson;
    }

//    获取所有评论列表
    @GetMapping( "/comment")
    public Object allComment(){
        return commentService.allComment();
    }

//    获得指定歌曲ID的评论列表
    @GetMapping( "/comment/song/detail")
    public Object commentOfSongId(HttpServletRequest req){
        String songId = req.getParameter("songId");
        return commentService.commentOfSongId(Integer.parseInt(songId));
    }

//    获得指定歌单ID的评论列表
    @GetMapping( "/comment/songList/detail")
    public Object commentOfSongListId(HttpServletRequest req){
        String songListId = req.getParameter("songListId");
        return commentService.commentOfSongListId(Integer.parseInt(songListId));
    }

//    点赞
    @PostMapping( "/comment/like")
    @ResponseBody
    public JSONObject commentOfLike(HttpServletRequest req){

    JSONObject resJson = new JSONObject();
    String id = req.getParameter("id").trim();
    String up = req.getParameter("up").trim();

    Comment comment = new Comment();
    comment.setId(Integer.parseInt(id));
    comment.setUp(Integer.parseInt(up));
    boolean res = commentService.updateCommentMsg(comment);
    if (res){
        resJson.put("code", 1);
        resJson.put("msg", "Successfully rated.");
    }else {
        resJson.put("code", 0);
        resJson.put("msg", "Failed to rate.");
    }
        return resJson;
    }

//    删除评论
    @GetMapping( "/comment/delete")
    public Object deleteComment(HttpServletRequest req){
        String id = req.getParameter("id");
        return commentService.deleteComment(Integer.parseInt(id));
    }

//    更新评论
    @ResponseBody
    @PostMapping("/comment/update")
    public JSONObject updateCommentMsg(HttpServletRequest req){
        JSONObject resJson = new JSONObject();
        String id = req.getParameter("id").trim();
        String userId = req.getParameter("userId").trim();
        String songId = req.getParameter("songId").trim();
        String songListId = req.getParameter("songListId").trim();
        String content = req.getParameter("content").trim();
        String type = req.getParameter("type").trim();
        String up = req.getParameter("up").trim();

        Comment comment = new Comment();
        comment.setId(Integer.parseInt(id));
        comment.setUserId(Integer.parseInt(userId));
        if ("".equals(songId)) {
            comment.setSongId(null);
        } else {
            comment.setSongId(Integer.parseInt(songId));
        }

        if ("".equals(songListId)) {
            comment.setSongListId(null);
        } else {
            comment.setSongListId(Integer.parseInt(songListId));
        }
        comment.setContent(content);
        comment.setType(Byte.valueOf(type));
        comment.setUp(Integer.parseInt(up));

        boolean res = commentService.updateCommentMsg(comment);
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
