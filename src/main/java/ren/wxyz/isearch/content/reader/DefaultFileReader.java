package ren.wxyz.isearch.content.reader;

import ren.wxyz.isearch.content.FileReader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 默认的文件读取器
 *
 * <br>
 * 本文件读取器，只读取绝对路径作为文件内容
 *
 * @author wxyz
 * @since 0.0.2
 */
public class DefaultFileReader implements FileReader {
    @Override
    public String[] getSupportExtName() {
        return new String[] {"*"};
    }

    @Override
    public String getContent(File file, Charset charset) throws IOException {
        return file.getCanonicalPath();
    }
}
