package com.l.tangshi.crawler.comment;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储清洗的数据
 */
@ToString
public class DataSet {
    /**
     * 用Map存储数据，最终取的是详情页面的数据
     * data将DOM解析，清洗之后存储的数据
     * Document Object Model 文档对象模型
     * Project Object Model  项目对象模型
     * 如：标题 XX，作者 XX，正文 XX
     */
    private Map<String,Object> data = new HashMap<>();
    //不让其它的类或者人随意操作data，
    // 若想要操作data，就必须通过putData()方法
    public void putData(String key, Object value){
        this.data.put(key,value);
    }
    //获取数据同理必须通过getData()方法
    public Object getData(String key) {
        return this.data.get(key);
    }

    /**
     * 获取所有的对象，通过Map
     * Map内部遍历data，重新放到HashMap里面，是一个新对象
     * 此时数据不怕破坏
     * @return
     */
    public Map<String,Object> getData(){
        return new HashMap<>(this.data);
    }
}
