package com.l.tangshi.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.l.tangshi.crawler.comment.Page;
import com.l.tangshi.crawler.parse.DataPageParse;
import com.l.tangshi.crawler.parse.DocumentParse;
import com.l.tangshi.crawler.parse.Parse;
import com.l.tangshi.crawler.pipeline.ConsolePipeline;
import com.l.tangshi.crawler.pipeline.Pipeline;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Crawler {
    //加上日志，打印异常输出信息
    private final Logger logger = LoggerFactory.getLogger(Crawler.class);
//    //文档队列，生产消费者模型，放置超链接
//    private final Queue<Page> docQueue = new LinkedBlockingQueue<>();
//    //放置详情页面，处理完成的数据，数据放在dataSet中
   private final Queue<Page> detailQueue = new LinkedBlockingQueue<>();

    //放置未被采集和解析的页面
    //Page  htmlPage  dataSet
    private final Queue<Page> docQueue = new LinkedBlockingQueue<>();
    //采集器
    private final WebClient webClient;
    //所有的解析器=>带三方
    private final List<Parse> parseList = new LinkedList<>();
    //所有的清洗器
    private final List<Pipeline> pipelineList = new LinkedList<>();
    //线程调度器
    private final ExecutorService executorService;

    public Crawler() {
        this.webClient = new WebClient(BrowserVersion.CHROME);
        this.webClient.getOptions().setJavaScriptEnabled(false);
        //创建线程执行器，创建线程工厂，设置自己的线程名称
        this.executorService = Executors.newFixedThreadPool(8, new ThreadFactory() {
            private final AtomicInteger id = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("Crawler-thread-" + id.getAndIncrement());
                return thread;
            }
        });
    }

    //爬虫的开始启动
    public void start() {
        this.executorService.submit(this::parse);
        this.executorService.submit(this::pipeline);
    }

    private void parse() {
        //死循环，一直解析，都从docQueue中去取
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                //e.printStackTrace();
                //{}表示占位符，会把getMessage()的内容放到{}里面
                //是一个可变参数，如果有n个{}，就可以传n个信息
                logger.error("Parse occur exception{}.",e.getMessage());
            }
            final Page page = this.docQueue.poll();
            //System.out.println("Parse =="+page);
            //如果队列是空的，返回null
            //先是只有：base, path, detail, 采集之后有了htmlPage
            if (page == null) {
                continue;
            }
            //将单线程的解析变成多线程
            this.executorService.submit(() -> {
                try {
                    //抓取数据，采集数据
                    HtmlPage htmlPage = Crawler.this.webClient.getPage(page.getUrl());
                    //解析完毕之后，就要放到详情队列里
                    page.setHtmlPage(htmlPage);
                    //匿名内部类：类名:XXX
                    for (Parse parse : Crawler.this.parseList) {
                        parse.parse(page);
                    }
                    if(page.isDetail()) {
                        //如果是详情页，加到详情队列里面去
                        Crawler.this.detailQueue.add(page);
                    } else {
                        //如果不是详情页，说明他有子页面，
                        // 找到子页面，取出来加到文档队列里面去，继续循环
                        Iterator<Page> iterator = page.getSubPage().iterator();
                        while (iterator.hasNext()) {
                            //这里的subPage来自于DocumentParse，实例化而来
                            Page subPage = iterator.next();
                            Crawler.this.docQueue.add(subPage);
                            iterator.remove();
                        }
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                    logger.error("Parse task occur exception{}.",e.getMessage());
                }
            });
        }
    }
    //管道清洗
    //发现是详情页就给清洗处理
    private void pipeline(){
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                //e.printStackTrace();
                logger.error("Parse occur exception{}.",e.getMessage());
            }
            final Page page = this.detailQueue.poll();
            if(page == null){
                continue;
            }
            //单线程变多线程
            this.executorService.submit(() -> {
                //每取一个数据走一遍清洗
                for(Pipeline pipeline : Crawler.this.pipelineList){
                    pipeline.pipeline(page);
                }
            });
        }
    }

    public void addPage(Page page) {
        this.docQueue.add(page);
    }

    public void addParse(Parse parse) {
        this.parseList.add(parse);
    }

    public void addPipeline(Pipeline pipeline) {
        this.pipelineList.add(pipeline);
    }

    //爬虫停止
    public void stop() {
        //线程池停了之后就不会工作，爬虫也就停止
        if (this.executorService != null && !this.executorService.isShutdown()) {
            this.executorService.shutdown();
        }
        logger.info("Crawler stopped...");
    }

//主函数tangshi=>
//    public static void main(String[] args) throws IOException {
//        //全程使用page，所以加上final，不让别人加以修改
//        final Page page = new Page("https://so.gushiwen.org",
//                "/gushi/tangshi.aspx", false);
//        Crawler crawler = new Crawler();
//        //文档解析器
//        crawler.addParse(new DocumentParse());
//        //数据解析器
//        crawler.addParse(new DataPageParse());
//        //管道->打印page里面的数据
//        crawler.addPipeline(new ConsolePipeline());
//        crawler.addPage(page);
//        //爬虫启动之后，需要给队列中加一个根文档，
//        // 所以再写一个方法addPage()
//        crawler.start();
//        //此页面是详情页，属性不加final时，可以使用set()方法
//        //page.getBase("https://so.gushiwen.org");
//        //page.getPath("/shiwenv_45c396367f59.aspx");
//        //page.setDetail(true);
//        //有page，然后解析
////        WebClient webClient = new WebClient(BrowserVersion.CHROME);
////        //禁用js文件
////        webClient.getOptions().setJavaScriptEnabled(false);
////        HtmlPage htmlPage = webClient.getPage(page.getUrl());
////        //page对象里面有了htmlPage文件
////        page.setHtmlPage(htmlPage);
////        //存放详情页面的队列
////        //Queue<Page> detailPageList = new LinkedBlockingQueue<>();
////        //解析器=>放到一个集合中去
////        List<Parse> parseList = new LinkedList<>();
////        parseList.add(new DocumentParse());
////        parseList.add(new DataPageParse());
////        //只是循环的话，并没有对数据做解析
////        //第一个页面解析完了之后，并没有对子页面进行解析
////        //解决办法：再写一个队列，存放详情page
//////        parseList.forEach(parse -> {
//////            parse.parse(page);
//////            //如果不是详情页，继续解析
//////            if(!page.isDetail()){
//////                //生产消费者模型，加到SubPage()中
//////                page.getSubPage();
//////            }
//////        });
////        System.out.println(page.getSubPage());
////
////        //清洗器=>放到一个集合中去
////        //可以写很多清洗的逻辑
//////        List<Pipeline> pipelineList = new LinkedList<>();
//////        pipelineList.add(new ConsolePipeline());
////
//////        Parse parse = new DataPageParse();
//////        //解析
//////        parse.parse(page);
//////        //管道清洗
//////        Pipeline pipeline = new ConsolePipeline();
//////        pipeline.pipeline(page);
////    }
//    }
}
