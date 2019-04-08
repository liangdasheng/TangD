package com.l.tangshi.analyze.service;

import com.l.tangshi.analyze.model.AuthorCount;
import com.l.tangshi.analyze.model.WordCount;

import java.util.List;

public interface AnalyzeService {
    /**
     * 作者的创作数量
     * @return
     */
    List<AuthorCount> analyzeAuthorCount();

    /**
     * 词云分析
     * @return
     */
    List<WordCount> analyzeWordCloud();
}
