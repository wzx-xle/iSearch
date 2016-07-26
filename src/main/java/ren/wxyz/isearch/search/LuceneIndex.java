package ren.wxyz.isearch.search;

import lombok.extern.slf4j.Slf4j;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lucence Index 操作
 *
 * @author wxyz
 * @since 0.0.2
 */
@Slf4j
public class LuceneIndex {

    /**
     * 建索器
     */
    private IndexWriter idxWriter;

    /**
     * 查询器
     */
    private IndexSearcher idxSearcher;

    /**
     * 创建一个索引
     * @param idxStoreDir 索引存储目录
     */
    public LuceneIndex(File idxStoreDir) throws IOException {
        if (!idxStoreDir.exists()) {
            idxStoreDir.mkdirs();
        }

        init(idxStoreDir);
    }

    /**
     * 初始化写索引器和读索引器
     *
     * @param idxStorePath 索引存储目录
     */
    private void init(File idxStorePath) throws IOException {
        try {
            // 打开目录
            Directory idxStoreDir = FSDirectory.open(idxStorePath);

            // 写索引器
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, new StandardAnalyzer(Version.LUCENE_47));
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            this.idxWriter = new IndexWriter(idxStoreDir, iwc);

            // 读索引器
            IndexReader idxReader = DirectoryReader.open(idxStoreDir);
            this.idxSearcher = new IndexSearcher(idxReader);
        }
        catch (IOException e) {
            close();
            log.warn("索引目录[{}]打开失败！MSG: {}", idxStorePath, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 更新索引
     *
     * @param doc 添加到索引的文档
     * @throws IOException
     */
    public void addDocument(Document doc) throws IOException {
        idxWriter.addDocument(doc);
        idxWriter.commit();
    }

    /**
     * 批量更新索引
     *
     * @param docs 批量添加到索引的文档
     * @throws IOException
     */
    public void addDocuments(List<Document> docs) throws IOException {
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
    public List<Document> search(Query query, int topN) throws IOException {
        // 查询
        TopDocs td = idxSearcher.search(query, topN);

        List<Document> docs = new ArrayList<>(topN);
        for (ScoreDoc sd : td.scoreDocs) {
            Document doc = idxSearcher.doc(sd.doc);
            docs.add(doc);
        }

        return docs;
    }

    /**
     * 删除文档
     *
     * @param queries 查询器
     */
    public void deleteDocuments(Query... queries) throws IOException {
        idxWriter.deleteDocuments(queries);
    }

    /**
     * 关闭服务
     */
    public synchronized void close() throws IOException {
        if (this.idxWriter != null) {
            this.idxWriter.close();
        }
    }
}
