package com.example.yin.domain;

import lombok.Data;

import java.io.Serializable;
@Data
public class Rank implements Serializable {

    private Long id;

    private Long songListId;

    private Long consumerId;

    private Integer score;

}
