package ren.wxyz.isearch.content;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

/**
 * 测试文件读取器工厂
 *
 * @author wxyz
 * @since 0.0.2
 */
public class FileReaderFoctoryTest {

    @Test
    public void testAll() throws Exception {
        FileReaderFoctory foctory = FileReaderFoctory.getFileReaderFoctory();
        assertNotNull(foctory.getSingleFileReader("*"));

        // 测试简单注册
        FileReader txtFileReader = new FileReader() {
            @Override
            public String[] getSupportExtName() {
                return new String[] {"txt"};
            }

            @Override
            public String getContent(File file, Charset charset) throws IOException {
                return "txt";
            }
        };
        foctory.registerFileReader(txtFileReader);
        assertEquals(txtFileReader, foctory.getSingleFileReader("txt"));

        // 测试覆盖注册
        FileReader txt2FileReader = new FileReader() {
            @Override
            public String[] getSupportExtName() {
                return new String[] {"txt"};
            }

            @Override
            public String getContent(File file, Charset charset) throws IOException {
                return "txt";
            }
        };
        foctory.registerFileReader(txt2FileReader);
        assertEquals(txt2FileReader, foctory.getSingleFileReader("txt"));

        // 测试多扩展名
        FileReader wordFileReader = new FileReader() {
            @Override
            public String[] getSupportExtName() {
                return new String[] {"doc", "docx"};
            }

            @Override
            public String getContent(File file, Charset charset) throws IOException {
                return "doc-docx";
            }
        };
        foctory.registerFileReader(wordFileReader);
        assertEquals(wordFileReader, foctory.getSingleFileReader("doc"));
        assertEquals(wordFileReader, foctory.getSingleFileReader("docx"));
    }
}