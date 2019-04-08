package com.l.tangshi.analyze.dao;

import com.l.tangshi.analyze.entity.PoetryInfo;
import com.l.tangshi.analyze.model.AuthorCount;

import java.util.List;

public interface AnalyzeDao {
    /**
     * 分析唐诗中作者创作的作品数量和作者的关系
     * 通过数据库查询出来的是key-value格式=》抽象成一个类
     * 功能：创作数量排行榜，词云图，高频词
     * 统计时需要把所有的诗文都查询出来，提供给业务层进行分析
     */
    List<AuthorCount> analyzeAuthorCount();
    List<PoetryInfo> queryAllPoetryInfo();
}
