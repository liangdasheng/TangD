package com.l.tangshi;

import com.alibaba.druid.pool.DruidDataSource;
import com.l.tangshi.analyze.dao.AnalyzeDao;
import com.l.tangshi.analyze.dao.impl.AnalyzeDaoImpl;
import com.l.tangshi.analyze.entity.PoetryInfo;
import com.l.tangshi.analyze.model.AuthorCount;
import com.l.tangshi.analyze.service.AnalyzeService;
import com.l.tangshi.analyze.service.impl.AnalyzeServiceImpl;
import com.l.tangshi.config.ConfigProperties;
import com.l.tangshi.config.ObjectFactory;
import com.l.tangshi.crawler.Crawler;
import com.l.tangshi.crawler.comment.Page;
import com.l.tangshi.crawler.parse.DataPageParse;
import com.l.tangshi.crawler.parse.DocumentParse;
import com.l.tangshi.crawler.pipeline.ConsolePipeline;
import com.l.tangshi.crawler.pipeline.DatabasePipeline;
import com.l.tangshi.web.WebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class tangshi {
    //加上日志
    private static final Logger LOGGER = LoggerFactory.getLogger(tangshi.class);

    public static void main(String[] args) {
        WebController webController = ObjectFactory.getInstance().getObject(WebController.class);
        LOGGER.info("Web Server launch...");
        //运行了web服务，提供接口
  //      webController.launch();

        //通过对象工厂的方式，启动爬虫
        //指定run-crawler的时候才会启动爬虫
 /*       if(args.length == 1 && args[0].equals("run-crawler")) {
            Crawler crawler = ObjectFactory.getInstance().getObject(Crawler.class);
            LOGGER.info("Crawler started...");
            crawler.start();
        }
    */
//        //web展示
//     Spark.get("/hello",(req,resp)->{
//         return "hello word"+LocalDateTime.now().toString() ;
//     }) ;



        //管道->打印page里面的数据
        //crawler.addPipeline(new ConsolePipeline());
        //爬虫启动之后，需要给队列中加一个根文档，
        // 所以再写一个方法addPage()
        //final Page page = new Page("https://so.gushiwen.org","/gushi/tangshi.aspx",false);
        //从配置文件去读，就会比较灵活，不会出现写死的现象
//        ConfigProperties configProperties = new ConfigProperties();
////        final Page page = new Page(
////                configProperties.getCrawlerBase(),
////                configProperties.getCrawlerPath(),
////                configProperties.isCrawlerDetail());
//        Crawler crawler = new Crawler();
//        //文档解析器
//        crawler.addParse(new DocumentParse());
//        //数据解析器
//        crawler.addParse(new DataPageParse());
//        DruidDataSource druidDataSource = new DruidDataSource();
//        druidDataSource.setUsername(configProperties.getDbUsername());
//        druidDataSource.setPassword(configProperties.getDbPassword());
//        druidDataSource.setDriverClassName(configProperties.getDbDriverClass());
//        druidDataSource.setUrl(configProperties.getDbUrl());

//        crawler.addPipeline(new DatabasePipeline(druidDataSource));
//        crawler.addPage(page);
//        crawler.start();

//        //测试AnalyzeDaoImpl
//        AnalyzeDao analyzeDao = new AnalyzeDaoImpl(druidDataSource);
//        System.out.println("test1:");
//        analyzeDao.analyzeAuthorCount()
//                .forEach(System.out::println);
//
//        System.out.println("test2:");
//        analyzeDao.queryAllPoetryInfo()
//                .forEach(System.out::println);

//        //测试AnalyzeServiceImpl降序
 //       AnalyzeDao analyzeDao = new AnalyzeDaoImpl(druidDataSource);
        //查询出所有的诗并打印出第一个诗
        //System.out.println(analyzeDao.queryAllPoetryInfo().get(0));
  //      AnalyzeService analyzeService = new AnalyzeServiceImpl(analyzeDao);
  //     analyzeService.analyzeWorldCloud().forEach(System.out::println);
    }
}
