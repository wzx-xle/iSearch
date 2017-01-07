/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.isearch;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import ren.wxyz.isearch.search.SearchService;
import ren.wxyz.isearch.support.AppConfig;
import ren.wxyz.isearch.ui.AbstractUI;
import ren.wxyz.isearch.ui.GraphicalUI;
import ren.wxyz.isearch.ui.ShellUI;

import java.io.File;
import java.io.IOException;

/**
 * 启动类
 *
 * @author wxyz
 * @since 0.1
 */
@Slf4j
public class App {
    /**
     * 版本号
     */
    private static final String APP_VERSION = "0.1";

    /**
     * 启动方法
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
//        File idxDir = new File("D:\\index-data");
//        if (!idxDir.exists()) {
//            idxDir.mkdirs();
//        }

//        createIndex(idxDir);

//        searchIndex(idxDir);

//        SearchService.setIndexDirectory(idxDir);
//        SearchService searchService = SearchService.getSingleInstance();
//
//        createIndex(searchService);
//
//        searchIndex(searchService);
//
//        searchService.close();

        log.info("启动程序");

        // 启动图形界面
        AbstractUI ui = null;

        // 设置命令行选项
        Options opts = new Options();
        opts.addOption("h", "help", false, "帮助信息");
        opts.addOption("c", "conf-file", true, "配置文件");
        opts.addOption("g", "graphical", false, "使用图形界面");
        opts.addOption("v", "version", false, "输出版本信息");

        // 解析命令行参数
        CommandLineParser parser = new DefaultParser();
        HelpFormatter help = new HelpFormatter();
        try {
            CommandLine cmd = parser.parse(opts, args);
            do {
                if (args.length == 0) {
                    help.printHelp("iSearch", opts);
                }
                else if (cmd.hasOption("h")) {
                    help.printHelp("iSearch", opts);
                    log.debug("查询帮助");
                }
                else if (cmd.hasOption("v")) {
                    ShellUI.println("iSearch v" + APP_VERSION);
                    log.debug("查询版本");
                }
                else {
                    // 设置配置文件
                    if (cmd.hasOption("c")) {
                        String configFile = cmd.getOptionValue("c");
                        if (!new File(configFile).exists()) {
                            ShellUI.println("配置文件找不到。");
                        }

                        AppConfig.getConfig().setConfigFilePath(configFile);
                        AppConfig.getConfig().init();
                        log.info("设置配置文件并初始化，配置文件路径：" + configFile);
                    }

                    // 设置启用图形界面
                    if (cmd.hasOption("g")) {
                        ui = new GraphicalUI();
                        log.info("启动图形界面");
                    }
                }
            }
            while (false);

            // 启动交互界面
            if (null == ui) {
                log.info("启动Shell界面");
                ui = new ShellUI();
            }
            ui.start(AppConfig.getConfig());
        }
        catch (ParseException e) {
            log.debug("命令行参数解析失败，args={}", args, e);
            help.printHelp("iSearch", opts);
        }
    }

    /**
     * 建索
     *
     * @param path 索引存储目录
     * @throws IOException
     */
    private static void createIndex(File path) throws IOException {
        // 配置建索器
        Directory idxDir = FSDirectory.open(path);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, new StandardAnalyzer(Version.LUCENE_47));
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        // 创建建索器
        IndexWriter idxWriter = new IndexWriter(idxDir, iwc);

        Document doc = new Document();
        doc.add(new TextField("path", "D:\\Workspace\\MyWeb1\\index.html", Field.Store.YES));
        doc.add(new TextField("content", "<html>index</html>", Field.Store.YES));
        idxWriter.addDocument(doc);

        doc = new Document();
        doc.add(new TextField("path", "D:\\Workspace\\MyWeb2\\选择模式.html", Field.Store.YES));
        doc.add(new TextField("content", "<html>选择模式</html>", Field.Store.YES));
        idxWriter.addDocument(doc);

        idxWriter.close();
    }

    private static void createIndex(SearchService searchService) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("path", "D:\\Workspace\\MyWeb1\\index.html", Field.Store.YES));
        doc.add(new TextField("content", "<html>index</html>", Field.Store.YES));
        searchService.addDocument(doc);

        doc = new Document();
        doc.add(new TextField("path", "D:\\Workspace\\MyWeb2\\选择模式.html", Field.Store.YES));
        doc.add(new TextField("content", "<html>选择模式</html>", Field.Store.YES));
        searchService.addDocument(doc);
    }

    private static void searchIndex(SearchService searchService) throws Exception {
        QueryParser parser = new QueryParser(Version.LUCENE_47, "path", new StandardAnalyzer(Version.LUCENE_47));
        Query query = parser.parse("myweb1");

        for (Document doc : searchService.search(query, 10)) {
            System.out.println(doc.get("path") + ": " + doc.get("content"));
        }
    }

    /**
     * 检索
     *
     * @param path 索引存储目录
     * @throws Exception
     */
    private static void searchIndex(File path) throws Exception {
        // 打开索引
        IndexReader idxReader = DirectoryReader.open(FSDirectory.open(path));
        IndexSearcher searcher = new IndexSearcher(idxReader);

        // 查询分析器
        QueryParser parser = new QueryParser(Version.LUCENE_47, "path", new StandardAnalyzer(Version.LUCENE_47));
        Query query = parser.parse("workspace");

        TopDocs td = searcher.search(query, 10);
        System.out.println("hits: " + td.totalHits);
        for (ScoreDoc sd : td.scoreDocs) {
            Document doc = searcher.doc(sd.doc);
            System.out.println(doc.get("path") + ": " + doc.get("content"));
        }
    }
}
