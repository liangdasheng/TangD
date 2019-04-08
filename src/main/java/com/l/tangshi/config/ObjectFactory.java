package com.l.tangshi.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.l.tangshi.analyze.dao.AnalyzeDao;
import com.l.tangshi.analyze.dao.impl.AnalyzeDaoImpl;
import com.l.tangshi.analyze.service.AnalyzeService;
import com.l.tangshi.analyze.service.impl.AnalyzeServiceImpl;
import com.l.tangshi.crawler.Crawler;
import com.l.tangshi.crawler.comment.Page;
import com.l.tangshi.crawler.parse.DataPageParse;
import com.l.tangshi.crawler.parse.DocumentParse;
import com.l.tangshi.crawler.pipeline.ConsolePipeline;
import com.l.tangshi.crawler.pipeline.DatabasePipeline;
import com.l.tangshi.web.WebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public final class ObjectFactory {
    //单例
    private static final ObjectFactory instance = new ObjectFactory();
    private final Logger logger = LoggerFactory.getLogger(ObjectFactory.class);
    /**
     * 对象比较多，所以放到一个Map中
     * 存放所有的对象
     */
    private final Map<Class, Object> objectHashMap = new HashMap<>();
    private ObjectFactory(){
        //初始化配置对象
        initConfigProperties();
        //创建数据源对象
        initDataSource();
        //爬虫对象
        initCrawler();
        //Web对象
        initWebController();
        //对象清单打印输出
        printObjectList();
    }

    private void initWebController() {
        DataSource dataSource = getObject(DataSource.class);
        AnalyzeDao analyzeDao = new AnalyzeDaoImpl(dataSource);
        AnalyzeService analyzeService = new AnalyzeServiceImpl(analyzeDao);
        WebController webController = new WebController(analyzeService);
        objectHashMap.put(WebController.class,webController);
    }

    private void initCrawler() {
        ConfigProperties configProperties = getObject(ConfigProperties.class);
        DataSource dataSource = getObject(DataSource.class);
        final Page page = new Page(
                configProperties.getCrawlerBase(),
                configProperties.getCrawlerPath(),
                configProperties.isCrawlerDetail());
        Crawler crawler = new Crawler();
        //文档解析器
        crawler.addParse(new DocumentParse());
        //数据解析器
        crawler.addParse(new DataPageParse());
        //如果是flase就不会在控制台上打印输出信息，否则会输出
        if(configProperties.isEnableConsole()) {
            crawler.addPipeline(new ConsolePipeline());
        }
        crawler.addPipeline(new DatabasePipeline(dataSource));
        crawler.addPage(page);
        //爬虫好了之后先不要启动，先把对象放到map中去
        objectHashMap.put(Crawler.class,crawler);
    }

    private void initDataSource() {
        ConfigProperties configProperties = getObject(ConfigProperties.class);
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername(configProperties.getDbUsername());
        druidDataSource.setPassword(configProperties.getDbPassword());
        druidDataSource.setDriverClassName(configProperties.getDbDriverClass());
        druidDataSource.setUrl(configProperties.getDbUrl());
        objectHashMap.put(DataSource.class,druidDataSource);
    }

    private void initConfigProperties() {
        ConfigProperties configProperties = new ConfigProperties();
        objectHashMap.put(ConfigProperties.class,configProperties);
        logger.info("ConfigProperties info:\n{}.",configProperties.toString());
    }

    public <T> T getObject(Class classz){
        if(!objectHashMap.containsKey(classz)){
            throw new IllegalArgumentException("Class"+classz.getName()+"not found Object");
        }
        return (T) objectHashMap.get(classz);
    }
    //单例
    public static ObjectFactory getInstance(){
        return instance;
    }

    private void printObjectList() {
        logger.info("************ObjectFactory List************");
        for(Map.Entry<Class,Object> entry : objectHashMap.entrySet()){
            logger.info(String.format("\t[%s] ==> [%s]",
                    entry.getKey().getCanonicalName(),
                    entry.getValue().getClass().getCanonicalName()));
        }
        logger.info("*****************************************");
    }
}
