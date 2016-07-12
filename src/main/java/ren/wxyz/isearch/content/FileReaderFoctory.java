package ren.wxyz.isearch.content;

import lombok.extern.slf4j.Slf4j;
import ren.wxyz.isearch.content.reader.DefaultFileReader;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件读取器工厂实例
 *
 * @author wxyz
 * @since 0.0.2
 */
@Slf4j
public class FileReaderFoctory {
    /**
     * 单一工厂
     */
    private static final FileReaderFoctory foctory = new FileReaderFoctory(new DefaultFileReader());

    /**
     * 得到一个文件读取器工厂
     *
     * @return 文件读取器工厂实例
     */
    public static FileReaderFoctory getFileReaderFoctory() {
        return foctory;
    }

    /**
     * 文件读取器集合，第一个参数是文件扩展名
     */
    private Map<String, FileReader> fileReaders = new HashMap<>();

    /**
     * 默认的文件读取器
     */
    private FileReader defaultFileReader;

    /**
     * 初始化文件读取器工厂实例
     *
     * @param defaultFileReader 默认的文件读取器
     */
    private FileReaderFoctory(FileReader defaultFileReader) {
        this.defaultFileReader = defaultFileReader;
        log.info("使用默认的文件读取器 {} 初始化", defaultFileReader.getClass());
    }

    /**
     * 注册一个文件读取器，相同的支持扩展名将覆盖之前注册过的
     *
     * @param fileReader 文件读取器
     */
    public void registerFileReader(FileReader fileReader) {
        String[] supportExtname = fileReader.getSupportExtName();
        if (supportExtname == null || supportExtname.length < 1) {
            return;
        }

        log.info("注册文件读取器 {} 到工厂中，支持扩展名：{}", fileReader.getClass(), fileReader.getSupportExtName());

        // 注册到集合中
        for (String extname : supportExtname) {
            if (fileReaders.containsKey(extname)) {
                fileReaders.remove(extname);
            }
            fileReaders.put(extname, fileReader);
        }
    }

    /**
     * 获取一个文件读取器
     *
     * @param extname 文件扩展名
     * @return 文件读取实例
     */
    public FileReader getSingleFileReader(String extname) {
        if (!fileReaders.containsKey(extname)) {
            return this.defaultFileReader;
        }
        return fileReaders.get(extname);
    }
}
