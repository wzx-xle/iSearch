/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.isearch.search;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import ren.wxyz.isearch.support.AppConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索服务，提供建索和搜索功能
 *
 * @author wxyz
 * @since 0.0.2
 */
public class SearchService {

    /**
     * 索引存储的目录
     */
    private static Directory idxDir;

    static {
        // 设置默认目录
        File defaultIndexDirectory = new File(AppConfig.DEFAULT_INDEX_DIRETORY);
        if (!defaultIndexDirectory.exists()) {
            defaultIndexDirectory.mkdirs();
        }
        try {
            idxDir = FSDirectory.open(defaultIndexDirectory);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置索引存储目录
     *
     * @param idxDir 索引存储目录
     */
    public static void setIndexDirectory(File idxDir) throws IOException {
        SearchService.idxDir = FSDirectory.open(idxDir);
    }

    /**
     * 获取单实例的搜索服务
     *
     * @return 搜索服务实例
     */
    public static SearchService getSingleInstance() {
        return Single.SINGLE;
    }

    /**
     * 建索器
     */
    private IndexWriter idxWriter;
    /**
     * 建索器配置
     */
    private IndexWriterConfig iwc;

    /**
     * 索引读取器
     */
    private IndexReader idxReader;
    private Object idxReaderLock = new Object();

    /**
     * 初始化实例
     */
    public SearchService() {
        // 配置建索器
        this.iwc = new IndexWriterConfig(Version.LUCENE_47, new StandardAnalyzer(Version.LUCENE_47));
        this.iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
    }

    /**
     * 更新索引
     *
     * @param doc 添加到索引的文档
     * @throws IOException
     */
    public synchronized void updateIndex(Document doc) throws IOException {
        if (this.idxWriter == null) {
            // 创建建索器
            idxWriter = new IndexWriter(this.idxDir, iwc);
        }
        idxWriter.addDocument(doc);
        idxWriter.commit();
    }

    /**
     * 批量更新索引
     *
     * @param docs 批量添加到索引的文档
     * @throws IOException
     */
    public synchronized void updateIndex(List<Document> docs) throws IOException {
        if (this.idxWriter == null) {
            // 创建建索器
            idxWriter = new IndexWriter(this.idxDir, iwc);
        }
        for (Document doc : docs) {
            idxWriter.addDocument(doc);
        }
        idxWriter.commit();
    }

    /**
     * 检索
     *
     * @param query 查询器
     * @param topN 前N条
     * @throws Exception
     */
    public List<Document> search(Query query, int topN) throws Exception {
        // 打开索引
        if (this.idxReader == null) {
            synchronized (this.idxReaderLock) {
                if (this.idxReader == null) {
                    this.idxReader = DirectoryReader.open(this.idxDir);
                }
            }
        }

        // 创建查询器
        IndexSearcher searcher = new IndexSearcher(idxReader);

        // 查询
        TopDocs td = searcher.search(query, topN);

        List<Document> docs = new ArrayList<>(topN);
        for (ScoreDoc sd : td.scoreDocs) {
            Document doc = searcher.doc(sd.doc);
            docs.add(doc);
        }

        return docs;
    }

    /**
     * 关闭服务
     */
    public synchronized void close() throws IOException {
        if (this.idxWriter != null) {
            idxWriter.close();
        }
    }

    /**
     * 单例创建类
     */
    private static class Single {
        final static SearchService SINGLE = new SearchService();
    }
}
