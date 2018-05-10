package com.db4J.glb;

import java.io.File;

/**
 * @author: love720720@163.com
 * @date: 2018年04月27日 11时06分
 */
public class Constants {

    private static final String DIR_CONFIG = "config";
    private static final String DIR_OUT = "out";
    private static final String DIR_JAVA = "java";
    private static final String DIR_SQL = "sql";
    public static final String CHARSET = "UTF-8";
    /**
     * 安装目录
     */
    public static final String DIR_USER = System.getProperty("user.dir");
    /**
     * 用户配置文件
     */
    public static final String FILE_UD_CONFIG = DIR_USER + File.separator + DIR_CONFIG + File.separator + "userConfig.ini";
    /**
     * 数据库配置文件
     */
    public static final String FILE_DB_CONFIG = DIR_USER + File.separator + DIR_CONFIG + File.separator + "dbConfig.ini";
    /**
     * 日志配置文件
     */
    public static final String FILE_LOG_CONFIG = DIR_USER + File.separator + DIR_CONFIG + File.separator + "logback.xml";
    /**
     * java输出目录
     */
    public static final String DIR_OUT_JAVA = DIR_OUT + File.separator + DIR_JAVA + File.separator ;
    /**
     * sql输出目录
     */
    public static final String DIR_OUT_SQL = DIR_OUT + File.separator + DIR_SQL + File.separator;
}
