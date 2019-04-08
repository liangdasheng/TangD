package com.l.tangshi.web;

import com.google.gson.Gson;
import com.l.tangshi.analyze.model.AuthorCount;
import com.l.tangshi.analyze.model.WordCount;
import com.l.tangshi.analyze.service.AnalyzeService;
import com.l.tangshi.config.ObjectFactory;
import com.l.tangshi.crawler.Crawler;
import lombok.Data;
import spark.*;
import sun.security.acl.WorldGroupImpl;

import java.util.List;

/**
 * Web API
 * Sparkjava实现 Web API 开发
 * 或者 Servlet实现 Web API 开发
 * Java-Httpd实现 Web API 开发，纯Java编写
 */
@Data
public class WebController {
    private final AnalyzeService analyzeService;

    public WebController(AnalyzeService analyzeService){
        this.analyzeService = analyzeService;
    }
    //url地址：http://127.0.0.1:4567/
    // /analyze/author_count
    private List<AuthorCount> analyzeAuthorCount(){
        return analyzeService.analyzeAuthorCount();
    }
    //url地址：http://127.0.0.1:4567/
    // analyze/word_cloud
    private List<WordCount> analyzeWordCloud(){
        return analyzeService.analyzeWordCloud();
    }
    //运行Web的方法
    public void launch(){
        ResponseTransformer transformer = new JSONResponseTransformer();
        //src/main/resources/static
        Spark.staticFileLocation("/static");
        Spark.get("/analyze/author_count",
                ((request, response) -> analyzeAuthorCount()), transformer);

        Spark.get("analyze/word_cloud",
                ((request, response) -> analyzeWordCloud()), transformer);

        Spark.get("/crawler/stop",
                ((request, response) -> { Crawler crawler = ObjectFactory
                        .getInstance().getObject(Crawler.class);
                crawler.stop();
                return "Crawler stopped...";
        }));
    }
    public static class JSONResponseTransformer implements ResponseTransformer{
        //Object->String
        //通过com.google.gson
        private Gson gson = new Gson();
        @Override
        public String render(Object o) throws Exception {
            return gson.toJson(o);
        }
    }

//    public static void main(String[] args) {
//        //字符串与对象之间的相互转换
//        //与序列化类似，序列化是把对象转换为byte[]数组
//        Gson gson = new Gson();
//        WordCount wordCount = new WordCount();
//        wordCount.setWord("java");
//        wordCount.setCount(10);
//        System.out.println(gson.toJson(wordCount));
//    }
}
