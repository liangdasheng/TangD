package com.l.tangshi.crawler.parse;

import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.l.tangshi.crawler.comment.Page;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 链接解析
 */
public class DocumentParse implements Parse {
    @Override
    public void parse(final Page page) {
        if(page.isDetail()){
            return;
        }
        //Lambda表达式，使用了final修饰，
        // 不能直接使用Integer修改，使用原子整型AtomicInteger
        //统计超链接出现的次数
        //final AtomicInteger count = new AtomicInteger(0);
        HtmlPage htmlPage = page.getHtmlPage();
        //选取div->class->typecont中为a的标签
        //链式调用，直接访问
        //元素名--属性名--属性值
        htmlPage.getBody()
                //这三个值是通过原古诗文网的文档模型分析出来的
                .getElementsByAttribute("div",
                        "class",
                        "typecont")
                .forEach(div->{
                  //消费型函数
                 //节点集合，第一次forEach取出所有的div，第二次forEach取出所有的a标签
                DomNodeList<HtmlElement> aNodeList = div.getElementsByTagName("a");
                aNodeList.forEach(aNode->{
                //a标签有很多，遍历之后，取节点的属性，即取出a标签的属性href
                String path = aNode.getAttribute("href");
                //每次找到一个超链接地址就count++
                //count.getAndIncrement();
                Page subPage = new Page(page.getBase(),path,true);
                //把网页遍历完了之后，把子页面加到当前页面的子页面中
                //获取当前文档的所有子页面，放到subPage中
                page.getSubPage().add(subPage);
            });
        });
    }
}
