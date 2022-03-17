package com.example.yin.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.yin.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@Controller
@ResponseBody
public class AdminController {
    @Autowired
    private AdminServiceImpl adminService;

    @PostMapping(value = "/admin/login/status")
    public JSONObject loginStatus(HttpServletRequest req, HttpSession session){
        JSONObject resJson = new JSONObject();
        String name = req.getParameter("name");
        String password = req.getParameter("password");

        boolean res = adminService.verifyPassword(name, password);
        if (res) {
            resJson.put("code", 1);
            resJson.put("msg", "successfully logged in.");
            session.setAttribute("name", name);
        } else {
            resJson.put("code", 0);
            resJson.put("msg", "Error in username or password.");
        }
        return resJson;

    }
}
