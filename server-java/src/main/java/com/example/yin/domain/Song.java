package com.example.yin.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Song {

    private Integer id;

    private Integer singerId;

    private String name;

    private String introduction;

    private Date createTime;

    private Date updateTime;

    private String pic;

    private String lyric;

    private String url;

}
