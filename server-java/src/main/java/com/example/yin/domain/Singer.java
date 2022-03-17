package com.example.yin.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Singer {

    private Integer id;

    private String name;

    private Byte sex;

    private String pic;

    private Date birth;

    private String location;

    private String introduction;


}
