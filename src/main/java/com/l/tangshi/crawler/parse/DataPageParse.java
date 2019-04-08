package com.l.tangshi.crawler.parse;

import com.gargoylesoftware.htmlunit.html.*;
import com.l.tangshi.analyze.entity.PoetryInfo;
import com.l.tangshi.crawler.comment.Page;

/**
 * 详情页面解析
 */
public class DataPageParse implements Parse{
    @Override
    public void parse(final Page page) {
        //假如不是详情页，直接return
        if(!page.isDetail()){
            return;
        }
        /**
         * 如果是详情页面，开始解析
         */

//        HtmlDivision htmlDivision = (HtmlDivision) page.getHtmlPage()
//                .getElementById("contson45c396367f59");
//        String content = htmlDivision.asText();
//        page.getDataSet().putData("正文",content);


        HtmlPage htmlPage = page.getHtmlPage();
        HtmlElement body = htmlPage.getBody();
        //标题
        //浏览器提供的copy->copy pathX等等方法，可以获取titlePath等
        //HtmlUnit官网->XPath queries->有各种方法可以查询
        //如果知道是一个的话，可以不写，也可以写下表为0；
        // 如果不知道的话，可以直接取下标
        //text()  直接把标签里面的元素转为字符串
        String titlePath = "//div[@class='cont']/h1/text()";
        //无论是文本还是超链接都会有一个Dom去接收
        //h1只有一个，所以直接用get(0)
        DomText titleDom = (DomText) body.getByXPath(titlePath).get(0);
        String title = titleDom.asText();
        //朝代
        String dynastyPath = "//div[@class='cont']/p/a[1]";
        HtmlAnchor dynastyDom = (HtmlAnchor) body.getByXPath(dynastyPath).get(0);
        String dynasty = dynastyDom.asText();
        //作者
        String authorPath = "//div[@class='cont']/p/a[2]";
        HtmlAnchor authorDom = (HtmlAnchor) body.getByXPath(authorPath).get(0);
        String author = authorDom.asText();
        //正文
        String contentPath = "//div[@class='cont']/div[@class='contson']";
        HtmlDivision contentDom = (HtmlDivision) body.getByXPath(contentPath).get(0);
        String content = contentDom.asText();

//        PoetryInfo poetryInfo = new PoetryInfo();
//        poetryInfo.setDynasty(dynasty);
//        poetryInfo.setAuthor(author);
//        poetryInfo.setTitle(title);
//        poetryInfo.setContent(content);
        //可以直接加更多的数据
        //但是是否清洗，由自己说了算
        page.getDataSet().putData("title",title);
        page.getDataSet().putData("dynasty",dynasty);
        page.getDataSet().putData("author",author);
        page.getDataSet().putData("content",content);

    }
}
