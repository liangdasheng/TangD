package com.l.tangshi;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.l.tangshi.analyze.entity.PoetryInfo;

import java.io.IOException;

public class TestHtmlUnit {
    public static void main(String[] args) {
        //模拟一个CHROME浏览器
        //try()  自动关闭流
        try(WebClient webClient = new WebClient(BrowserVersion.CHROME)){
            try {
                //禁止启用js文件：false
                webClient.getOptions().setJavaScriptEnabled(false);
                //采集网页上的数据
                //HtmlUnit官网 查询如何=>采集网页上面的数据
                // getPage()  传的就是url
                HtmlPage htmlPage = webClient.getPage("https://so.gushiwen.org/shiwenv_45c396367f59.aspx");
                //HtmlElement bodyElement = htmlPage.getBody();
                //asText() 只是取出网页中的文本
                //String text = bodyElement.asText();
                //System.out.println(text);
                //asXml()  取出网页中的结构，包括节点
                //String text = bodyElement.asXml();
                //System.out.println(text);
                //getElementById()  通过id获取内容
                //获取DOM Tree, DOM节点
                //查看具体文本，采集数据
                //DomElement domElement = htmlPage.getElementById("contson6f0532a0f8af");
                //查看具体类型->com.gargoylesoftware.htmlunit.html.HtmlDivision
                //System.out.println(domElement.getClass().getName());
                //解析数据
                //String divContent = domElement.asText();
                //System.out.println(divContent);
                HtmlElement body = htmlPage.getBody();
//                //标题
//                String titlePath = "//div[@class='cont']/h1/text()";
//                DomText titleDom = (DomText) body.getByXPath(titlePath).get(0);
//                String title = titleDom.asText();
//                System.out.println(title);
//                //朝代
//                String dynastyPath = "//div[@class='cont']/p/a[1]";
//                HtmlAnchor dynastyDom = (HtmlAnchor) body.getByXPath(dynastyPath).get(0);
//                String dynasty = dynastyDom.asText();
//                System.out.println(dynasty);
//                //作者
//                String authorPath = "//div[@class='cont']/p/a[2]";
//                HtmlAnchor authorDom = (HtmlAnchor) body.getByXPath(authorPath).get(0);
//                String author = authorDom.asText();
//                System.out.println(author);
//                //正文
//                //不能使用id，因为不同的详情页id可能相同
//                String contentPath = "//div[@class='cont']/div[@class='contson']";
//                //循环输出，检查第一个是否为期望的结果，如果是，确定下标是第一个
////                body.getByXPath(contentPath).forEach(o->{
////                    HtmlDivision division = (HtmlDivision) o;
////                    System.out.println(division.asText());
////                    System.out.println("---------------------------");
////                });
//                HtmlDivision contentDom = (HtmlDivision) body.getByXPath(contentPath).get(0);
//                String content = contentDom.asText();
//                System.out.println(content);
//                //取得所有的数据之后，数据都是零散的，然后把他们放到一个类中

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
