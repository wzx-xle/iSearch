package ren.wxyz.isearch.content.reader;

import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

/**
 * 测试默认文件读取器
 *
 * @author wxyz
 * @since 0.0.2
 */
public class DefaultFileReaderTest {

    @Test
    public void testGetSupportExtName() throws Exception {
        DefaultFileReader defaultFileReader = new DefaultFileReader();
        assertEquals("*", defaultFileReader.getSupportExtName()[0]);
    }

    @Test
    public void testGetContent() throws Exception {
        DefaultFileReader defaultFileReader = new DefaultFileReader();

        String path = "D:\\Workspace\\IdeaProjects\\iSearch\\src\\test\\resouces\\testfilereader.txt";
        assertEquals(path, defaultFileReader.getContent(new File(path), Charset.defaultCharset()));
    }
}