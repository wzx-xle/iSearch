package ren.wxyz.isearch.search;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.junit.Assert;
import org.junit.Test;
import ren.wxyz.isearch.support.FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试lucence索引封装类
 *
 * @author wxyz
 * @since 0.0.3
 */
public class LuceneIndexTest {

    @Test
    public void testAddDocument() throws Exception {
        File storeDirectory = new File(LuceneIndexTest.class.getResource("/").getFile(), "idxDir" + System.currentTimeMillis());
        LuceneIndex luceneIndex = new LuceneIndex(storeDirectory);

        // 打开写索引器
        luceneIndex.openWriter(Version.LUCENE_47, new StandardAnalyzer(Version.LUCENE_47));

        Document doc = new Document();
        doc.add(new TextField("path", "D:\\Workspace\\MyWeb1\\index.html", Field.Store.YES));
        doc.add(new TextField("content", "<html>index</html>", Field.Store.YES));
        luceneIndex.addDocument(doc);

        doc = new Document();
        doc.add(new TextField("path", "D:\\Workspace\\MyWeb2\\选择模式.html", Field.Store.YES));
        doc.add(new TextField("content", "<html>选择模式</html>", Field.Store.YES));
        luceneIndex.addDocument(doc);

        luceneIndex.closeWriter();

        Assert.assertTrue(FileHelper.delete(storeDirectory));
    }

    @Test
    public void testAddDocuments() throws Exception {
        File storeDirectory = new File(LuceneIndexTest.class.getResource("/").getFile(), "idxDir" + System.currentTimeMillis());
        LuceneIndex luceneIndex = new LuceneIndex(storeDirectory);

        List<Document> docs = new ArrayList<>();

        Document doc = new Document();
        doc.add(new TextField("path", "D:\\Workspace\\MyWeb1\\index.html", Field.Store.YES));
        doc.add(new TextField("content", "<html>index</html>", Field.Store.YES));
        docs.add(doc);

        doc = new Document();
        doc.add(new TextField("path", "D:\\Workspace\\MyWeb2\\选择模式.html", Field.Store.YES));
        doc.add(new TextField("content", "<html>选择模式</html>", Field.Store.YES));
        docs.add(doc);

        luceneIndex.openWriter(Version.LUCENE_47, new StandardAnalyzer(Version.LUCENE_47));
        luceneIndex.addDocuments(docs);
        luceneIndex.closeWriter();

        Assert.assertTrue(FileHelper.delete(storeDirectory));
    }

    @Test
    public void testSearch() throws Exception {
        File storeDirectory = new File(LuceneIndexTest.class.getResource("/").getFile(), "idxDir" + System.currentTimeMillis());
        LuceneIndex luceneIndex = new LuceneIndex(storeDirectory);

        List<Document> docs = new ArrayList<>();

        Document doc = new Document();
        doc.add(new TextField("path", "D:\\Workspace\\MyWeb1\\index.html", Field.Store.YES));
        doc.add(new TextField("content", "<html>index</html>", Field.Store.YES));
        docs.add(doc);

        doc = new Document();
        doc.add(new TextField("path", "D:\\Workspace\\MyWeb2\\选择模式.html", Field.Store.YES));
        doc.add(new TextField("content", "<html>选择模式</html>", Field.Store.YES));
        docs.add(doc);

        luceneIndex.openWriter(Version.LUCENE_47, new StandardAnalyzer(Version.LUCENE_47));
        luceneIndex.addDocuments(docs);
        luceneIndex.closeWriter();

        luceneIndex.openSearcher();
        QueryParser parser = new QueryParser(Version.LUCENE_47, "path", new StandardAnalyzer(Version.LUCENE_47));
        Query query = parser.parse("MyWeb1");

        List<Document> res = luceneIndex.search(query, 1);
        Assert.assertEquals(1, res.size());
        Assert.assertEquals("D:\\Workspace\\MyWeb1\\index.html", res.get(0).get("path"));
        Assert.assertEquals("<html>index</html>", res.get(0).get("content"));

        parser = new QueryParser(Version.LUCENE_47, "path", new StandardAnalyzer(Version.LUCENE_47));
        query = parser.parse("MyWeb2");

        res = luceneIndex.search(query, 1);
        Assert.assertEquals(1, res.size());
        Assert.assertEquals("D:\\Workspace\\MyWeb2\\选择模式.html", res.get(0).get("path"));
        Assert.assertEquals("<html>选择模式</html>", res.get(0).get("content"));

        parser = new QueryParser(Version.LUCENE_47, "content", new StandardAnalyzer(Version.LUCENE_47));
        query = parser.parse("选择模式");

        res = luceneIndex.search(query, 1);
        Assert.assertEquals(1, res.size());
        Assert.assertEquals("D:\\Workspace\\MyWeb2\\选择模式.html", res.get(0).get("path"));
        Assert.assertEquals("<html>选择模式</html>", res.get(0).get("content"));


        Assert.assertTrue(FileHelper.delete(storeDirectory));
    }

    @Test
    public void testDeleteDocuments() throws Exception {

    }
}