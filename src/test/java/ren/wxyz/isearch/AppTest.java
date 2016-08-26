/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.isearch;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 测试启动类
 *
 * @auther wxyz 2016-08-25_19:58
 * @since 0.1
 */
public class AppTest {

    @Test
    public void testMain() throws Exception {
        App.main(new String[]{
        });

        App.main(new String[]{
                "-h"
        });

        App.main(new String[]{
                "--help"
        });

        App.main(new String[]{
                "-v"
        });

        App.main(new String[]{
                "--version"
        });
    }
}