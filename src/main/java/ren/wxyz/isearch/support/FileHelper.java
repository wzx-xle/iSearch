package ren.wxyz.isearch.support;

import java.io.File;

/**
 * 文件工具类
 *
 * @author wxyz
 * @since 0.0.3
 */
public class FileHelper {

    /**
     * 删除文件或目录
     *
     * @param path 文件路径
     * @return 删除状态
     */
    public static boolean delete(File path) {
        if (null == path) {
            return false;
        }

        if (path.isDirectory()) {
            File[] subFiles = path.listFiles();

            // 删除目录下的文件
            for (File subFile : subFiles) {
                // 递归删除
                if (!delete(subFile)) {
                    return false;
                }
            }
        }

        if (!path.delete()) {
            return false;
        }

        return true;
    }
}
