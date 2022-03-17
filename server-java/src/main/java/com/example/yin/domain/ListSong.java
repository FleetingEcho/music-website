package com.example.yin.domain;

import lombok.Data;

import java.io.Serializable;
@Data
public class ListSong implements Serializable {

    private Integer id;

    private Integer songId;

    private Integer songListId;

}
