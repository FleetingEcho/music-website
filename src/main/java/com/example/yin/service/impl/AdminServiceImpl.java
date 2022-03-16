package com.example.yin.service.impl;

import com.example.yin.dao.AdminMapper;
import com.example.yin.service.AdminService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public boolean veritypasswd(@Param("name") String name,@Param("password") String password) {

        return adminMapper.verifyPassword(name, password)>0?true:false;
    }
}
