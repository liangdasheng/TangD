package com.l.tangshi.crawler.pipeline;

import com.l.tangshi.crawler.comment.Page;

/**
 * 管道=》清洗
 */
public interface Pipeline {
    void pipeline(final Page page);
}
