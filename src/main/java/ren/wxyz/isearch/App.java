package ren.wxyz.isearch;

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

import java.io.File;
import java.io.IOException;

/**
 * 启动类
 *
 * @author wxyz
 * @since 0.0.1
 */
public class App {
    /**
     * 启动方法
     *
     * @param args 启动参数
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");

        File idxDir = new File("D:\\index-data");
        if (!idxDir.exists()) {
            idxDir.mkdirs();
        }

        createIndex(idxDir);

        searchIndex(idxDir);
    }

    /**
     * 建索
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

    /**
     * 检索
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
