package com.l.tangshi.crawler.comment;

import lombok.Data;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.HashSet;
import java.util.Set;

@Data
/**
 * 存放公共类
 * @Getter
 * @Setter => 利用lombok直接生成代码
 * 或者使用 @Data()=>getter(),setter(),toString(),HashCode(),equals()方法 也会直接生成好
 */
public class Page {
    /**
     *数据网站的根地址
     * 如：https://so.gushiwen.org
     */
    private final String base;
    /**
     * 具体网页的路径
     * 如：/shiwenv_45c396367f59.aspx
     * 网页的url = base + path
     */
    private final String path;
    /**
     * 判断是否是详情页
     * 若不是，则一定会有子页面
     */
    private final boolean detail;

    /**
     * 创建子页面对象合集
     */
    private Set<Page> subPage = new HashSet<>();

    /**
     * 网页的DOM对象
     * htmlPage  文档对象模型
     */
    private HtmlPage htmlPage;

    /**
     * 数据对象=>comment包=>DataSet类
     * 最终需要把数据提取出来=>dataSet
     * 实际上就是一个Map<>，单独包装出来一个类
     */
    private DataSet dataSet = new DataSet();

    /**
     * 网页的 全地址url 就是 根地址base + 具体路径path
     */
    public String getUrl(){
        return this.base+this.path;
    }

}
