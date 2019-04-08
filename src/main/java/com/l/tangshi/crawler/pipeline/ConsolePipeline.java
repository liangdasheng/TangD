package com.l.tangshi.crawler.pipeline;

import com.l.tangshi.crawler.comment.Page;

import java.util.Map;

/**
 * 控制台，输出数据
 */
public class ConsolePipeline implements Pipeline{
    @Override
    public void pipeline(final Page page) {
        Map<String,Object> data = page.getDataSet().getData();
        /**
         * 存储
         * 最终的数据
         * 想怎么变就怎么变
         */
        System.out.println(data);
    }
}
