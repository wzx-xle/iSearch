package ren.wxyz.isearch.search;

import lombok.Getter;
import lombok.Setter;

/**
 * 文件的信息
 *
 * @author wxyz
 * @since 0.0.2
 */
@Getter
@Setter
public class FileInfo {

    /**
     * 本地路径，使用据对路径并包括文件名
     */
    private String localPath;

    /**
     * 本地文件的Hash值，这里采用sha-256
     */
    private String hash;

    /**
     * 文件的扩展名
     */
    private String extName;

    /**
     * 本地文件的内容
     */
    private String fileContent;

    /**
     * 文档的修改日期，格式为 yyyyMMddHHmmss
     */
    private String modifyTime;
}
