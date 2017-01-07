/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.isearch.ui;

/**
 * Shell 用户接口
 *
 * @auther wxyz 2016-08-25_19:18
 * @since 0.1
 */
public class ShellUI extends AbstractUI {

    @Override
    protected void start() {

    }

    /**
     * 输出信息到命令行
     * @param msg 信息
     */
    public static void println(String msg) {
        System.out.println(msg);
    }
}
