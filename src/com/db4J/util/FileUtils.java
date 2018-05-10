package com.db4J.util;

import com.db4J.exp.BaseException;

import java.io.File;

/**
 *
 * @author love720720@163.com
 * @date 2017年5月15日 下午3:35:09
 */
public class FileUtils {

    /**
     * 创建目录
     * @param dir
     * @return
     */
    public static void mkdirs(String dir) throws BaseException {
        File file = new File(dir);
        // 判断目录是否存在
        if (!file.exists()) {
            return;
        }
        // 结尾是否以"/"结束
        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }
        // 创建目标目录
        if (file.mkdirs()) {
            throw new BaseException("目录创建失败");
        }
    }
}
