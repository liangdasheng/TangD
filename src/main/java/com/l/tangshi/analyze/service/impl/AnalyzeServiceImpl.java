package com.l.tangshi.analyze.service.impl;

import com.l.tangshi.analyze.dao.AnalyzeDao;
import com.l.tangshi.analyze.entity.PoetryInfo;
import com.l.tangshi.analyze.model.AuthorCount;
import com.l.tangshi.analyze.model.WordCount;
import com.l.tangshi.analyze.service.AnalyzeService;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.util.*;

/**
 * (1) 查询出所有的数据
 * (2) 取出title, content
 * (3) 分词，过滤 /w null 空 len < 2
 * (4) 统计 k-v k是词，v是词频
 */
public class AnalyzeServiceImpl implements AnalyzeService {

    private final AnalyzeDao analyzeDao;

    public AnalyzeServiceImpl(AnalyzeDao analyzeDao) {
        this.analyzeDao = analyzeDao;
    }

    /**
     * 这个统计没有做排序
     * 排序方式有两种：
     * 1. DAO层SQL排序=》order by
     * 2. Service 层进行数据排序
     * @return
     */
    @Override
    public List<AuthorCount> analyzeAuthorCount() {
        List<AuthorCount> authorCounts = analyzeDao.analyzeAuthorCount();
        //升序，实现Comparable接口
        //return o1.getCount() - o2.getCount();
        //*(-1) 就是降序，不*(-1)就是升序
        Collections.sort(authorCounts, Comparator.comparing(AuthorCount::getCount));
        return authorCounts;
    }

    @Override
    /**
     * 查询出所有的数据，取出title content；
     * 分词；统计key-value，词-词频
     * 过滤：词性为 w 的不要，为null的不要，
     * 并且len < 2的不要等等，自己需要分析
     */
    public List<WordCount> analyzeWordCloud() {
        Map<String,Integer>map = new HashMap<>();
        List<PoetryInfo> poetryInfos = analyzeDao.queryAllPoetryInfo();
        for(PoetryInfo poetryInfo : poetryInfos){
            List<Term> terms = new ArrayList<>();
            String title = poetryInfo.getTitle();
            String content = poetryInfo.getContent();
            terms.addAll(NlpAnalysis.parse(title).getTerms());
            terms.addAll(NlpAnalysis.parse(content).getTerms());
            //过滤集合,使用stream流或者集合过滤，此处使用集合过滤
            //ArrayList是并发修改的，不能直接修改，应该过滤并删除
            //所以这里使用迭代器
            Iterator<Term> iterator = terms.iterator();
            while (iterator.hasNext()){
                Term term = iterator.next();
                //词性的过滤：假如term的词性为null或者为w，就把他remove
                if(term.getNatureStr() == null || term.getNatureStr().equals("w")){
                    iterator.remove();
                    continue;
                }
                //词的过滤:假如词的长度小于2 就移除
                if(term.getRealName().length() < 2){
                    iterator.remove();
                    continue;
                }
                //统计
                String realName = term.getRealName();
                int count;
                if(map.containsKey(realName)){
                    count = map.get(realName) + 1;
                }else{
                    count = 1;
                }
                map.put(realName,count);
            }
        }
        //因为map返回值类型不匹配，所以进行此步骤进行转换
        List<WordCount> wordCounts = new ArrayList<>();
        for(Map.Entry<String,Integer> entry : map.entrySet()){
            WordCount wordCount = new WordCount();
            wordCount.setCount(entry.getValue());
            wordCount.setWord(entry.getKey());
            //最后一定要添加到集合中才能打印出来
            wordCounts.add(wordCount);
        }
        return wordCounts;
    }
//    //测试  分词
//    public static void main(String[] args) {
//        Result result = NlpAnalysis.parse("持此谢高鸟，因之传远情");
//        List<Term> terms = result.getTerms();
//        for(Term term : terms){
//            System.out.println(term);
//        }
//    }
}
