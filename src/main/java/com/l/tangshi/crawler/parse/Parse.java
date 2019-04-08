package com.l.tangshi.crawler.parse;
import com.l.tangshi.crawler.comment.Page;

public interface Parse {
    //解析页面=>解析器=>最后由它解析
    void parse(final Page page);
}
