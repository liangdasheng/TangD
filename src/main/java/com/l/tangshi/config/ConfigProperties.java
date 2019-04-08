package com.l.tangshi.config;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置分析
 */
@Data
public class ConfigProperties {
    private final Logger logger = LoggerFactory.getLogger(ConfigProperties.class);
    private String crawlerBase;
    private String crawlerPath;
    private boolean crawlerDetail;

    private String dbUsername;
    private String dbPassword;
    private String dbUrl;
    private String dbDriverClass;

    private boolean enableConsole;

    public ConfigProperties(){
        //从外部文件加载
        InputStream inputStream = ConfigProperties.class.getClassLoader()
                .getResourceAsStream("config.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            //System.out.println(properties);
        } catch (IOException e) {
            //e.printStackTrace();
            logger.error("ConfigProperties occur exception{}.",e.getMessage());
        }
        this.crawlerBase = String.valueOf(properties.get("crawler.base"));
        this.crawlerPath = String .valueOf(properties.get("crawler.path"));
        this.crawlerDetail = Boolean.parseBoolean(String.valueOf(properties.get("crawler.detail")));
        //剩下的四个
        this.dbUsername = String.valueOf(properties.get("db.username"));
        this.dbPassword = String.valueOf(properties.get("db.password"));
        this.dbUrl = String.valueOf(properties.get("db.url"));
        this.dbDriverClass = String.valueOf(properties.get("db.driver_class"));

        this.enableConsole = Boolean.valueOf(String.valueOf
                (properties.getProperty("config.enable_console","false")));
    }

    public static void main(String[] args) {
        new ConfigProperties();
    }
}
