package ren.wxyz.isearch.support;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 测试配置文件类
 *
 * @author wxyz
 * @since 0.0.2
 */
public class AppConfigTest {

    @Test
    public void testGetConfig() throws Exception {
        assertEquals("./idxDir", AppConfig.getConfig().getIndexStoreDirectory());
    }
}