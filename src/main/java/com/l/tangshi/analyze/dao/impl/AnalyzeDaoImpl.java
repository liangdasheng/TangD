package com.l.tangshi.analyze.dao.impl;

// ORM框架：mybatis, hibernate, JOOQ, TopLink, Spring-Data-JDBC，
// 工具：dbUtils
// 通过反射机制得到对象关系映射，使得代码简化

import com.l.tangshi.analyze.dao.AnalyzeDao;
import com.l.tangshi.analyze.entity.PoetryInfo;
import com.l.tangshi.analyze.model.AuthorCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 实现
 */

public class AnalyzeDaoImpl implements AnalyzeDao {
    private final Logger logger = LoggerFactory.getLogger(AnalyzeDaoImpl.class);
    //JDBC编程，首先准备数据源
    private final DataSource dataSource;

    public AnalyzeDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<AuthorCount> analyzeAuthorCount() {
        List<AuthorCount> datas = new ArrayList<>();
        //try()  自动关闭
        String sql = "select count(*) as count,author from poetry_info group by author;";
        //try()  自动关闭
        try(Connection connection = dataSource.getConnection();
            //预编译
            PreparedStatement statement = connection.prepareStatement(sql);
            //执行查询，获取结果
            ResultSet resultSet = statement.executeQuery()
        ){
            while (resultSet.next()){
                AuthorCount authorCount = new AuthorCount();
                authorCount.setAuthor(resultSet.getString("author"));
                authorCount.setCount(resultSet.getInt("count"));
                //把数据放到集合里面去
                datas.add(authorCount);
            }
        }catch (SQLException e){
            //e.printStackTrace();
            logger.error("Database query occur exception{}.",e.getMessage());
        }
        return datas;
    }

    @Override
    public List<PoetryInfo> queryAllPoetryInfo() {
        //一组诗放到一个集合里面去
        List<PoetryInfo> datas = new ArrayList<>();
        String sql = "select title,dynasty,author,content from poetry_info;";
        //数据库连接  预编译命令  结果集  执行查询
        try(Connection connection  = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ){
            while (resultSet.next()){
                PoetryInfo poetryInfo = new PoetryInfo();
                poetryInfo.setTitle(resultSet.getString("title"));
                poetryInfo.setDynasty(resultSet.getString("dynasty"));
                poetryInfo.setAuthor(resultSet.getString("author"));
                poetryInfo.setContent(resultSet.getString("content"));
                datas.add(poetryInfo);
            }
        }catch (SQLException e){
            //e.printStackTrace();
            logger.error("Database query occur exception{}.",e.getMessage());
        }
        return datas;
    }
}
