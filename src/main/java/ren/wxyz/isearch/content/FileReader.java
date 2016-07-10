package ren.wxyz.isearch.content;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 将其他文件转为文本内容
 *
 * @author wxyz
 * @since 0.0.2
 */
public interface FileReader {

    /**
     * 获取支持的扩展名集合
     *
     * @return 支持的扩展名集合
     */
    String[] getSupportExtName();

    /**
     * 读取文件内容
     * @param file 文件路径
     * @param charset 文件编码
     * @return 文件的内容
     * @throws IOException 文件读取异常
     */
    String getContent(File file, Charset charset) throws IOException;
}
