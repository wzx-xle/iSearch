/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.isearch.ui;

import lombok.Setter;
import ren.wxyz.isearch.support.AppConfig;

/**
 * 公共的交互界面类
 *
 * @auther wxyz 2016-08-25_20:30
 * @since 0.1
 */
@Setter
public abstract class AbstractUI {

    /**
     * 应用配置
     */
    protected AppConfig appConfig;

    /**
     * 启动UI
     *
     * @param appConfig 应用配置
     */
    public void start(AppConfig appConfig) {
        this.appConfig = appConfig;
        start();
    }

    /**
     * 启动UI
     */
    public abstract void start();
}
