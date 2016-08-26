package ren.wxyz.isearch.support;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 应用程序配置
 *
 * @author wxyz
 * @since 0.0.2
 */
@Getter
@Setter
public class AppConfig {
    /**
     * 默认索引存储目录
     */
    public final static String DEFAULT_INDEX_STORE_DIRETORY = "./idxDir";

    /**
     * 单例化配置
     */
    private final static AppConfig config = new AppConfig();

    /**
     * 获取配置对象
     *
     * @return 应用配置对象
     */
    public static AppConfig getConfig() {
        return config;
    }

    /**
     * 配置文件路径
     */
    private String configFilePath;

    /**
     * 索引存储目录
     */
    private String indexStoreDirectory;

    /**
     * 初始化参数
     */
    private AppConfig() {
        Properties props = readProperties("/isearch.conf");

        this.indexStoreDirectory = props.getProperty("index.store", DEFAULT_INDEX_STORE_DIRETORY);
    }

    /**
     * 初始化应用配置
     */
    public void init() {

    }

    public void save() {

    }

    /**
     * 读取配置文件到属性
     * @param confFilename 配置文件名
     * @return 读取的配置属性
     */
    private Properties readProperties(String confFilename) {
        Properties props = new Properties();

        try (InputStream is = AppConfig.class.getResourceAsStream(confFilename)) {
            props.load(new InputStreamReader(is, "utf-8"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return props;
    }
}
