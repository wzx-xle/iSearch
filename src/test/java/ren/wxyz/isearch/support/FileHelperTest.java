package ren.wxyz.isearch.support;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * 文件帮助类测试
 *
 * @author wxyz
 * @since 0.0.3
 */
public class FileHelperTest {

    @Test
    public void testDelete() throws Exception {
        File path = new File(FileHelperTest.class.getResource("/").getFile(), "test_delete");
        FileHelper.delete(path);

        assertFalse(path.exists());
    }
}