package com.example.yin.dao;

import com.example.yin.domain.Rank;
import org.springframework.stereotype.Repository;

@Repository
public interface RankMapper {

    int insert(Rank record);

    int insertSelective(Rank record);

    int selectScoreSum(Long songListId);

    int selectRankNum(Long songListId);
}
