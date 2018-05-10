package com.db4J.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.db4J.exp.BaseException;
import com.db4J.glb.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 *
 * @author love720720@163.com
 * @date 2017年9月1日 下午4:53:11
 */
public class JavaUtils {

	private static Logger log = LogUtils.getLogger();
	
	/**
	 * 创建java文件
	 * @param tableName
	 * @return
	 * @throws BaseException
	 * @throws Exception
	 */
	public static String createFile(String tableName) throws BaseException, Exception {
		log.info("开始创建java文件");
		long startTimeMillis = System.currentTimeMillis();

		// 获取数据库字段
		List<Map<String, Object>> list = DbUtils.getColumnNames(tableName);

        Map<String, String> userConfigMap = DbUtils.getUserConfigMap();
        // 获取作者
		String author = userConfigMap.get("author");
		// 获取包路径
		String pack = userConfigMap.get("pack");
		// 获取文件路径
		String dir = userConfigMap.get("dirJava");

		String className = columnNameToDeclareVar(tableName);

		DateFormat df = new SimpleDateFormat("yyyy年M月d日 ah:mm:ss");
		String date = df.format(new Date());

        StringBuffer sb = new  StringBuffer();
		sb.append("package ");
		sb.append(pack).append(";");
		sb.append("\r\n");
		sb.append("\r\n");
		sb.append("/**").append("\r\n");
		sb.append(" *").append("\r\n");
		sb.append(" *").append(" @author ").append(author).append("\r\n");
		sb.append(" *").append(" @date ").append(date).append("\r\n");
		sb.append(" */");
		sb.append("\r\n");
		sb.append("public class "+ className + " {\r\n");

		log.info("开始创建私有属性");
		int size;
		for (int i = 0; i < (size = list.size()); i++) {
			Map<String, Object> map = list.get(i);
			String columnName = columnNameToDeclareVar(map.get("columnName").toString());
			String columnType = map.get("columnType").toString();
			String columnSize = map.get("dataSize").toString();
			String remarks = map.get("remarks").toString();
			String javaType = oracleSqlType2JavaType(columnType.toLowerCase(),Integer.parseInt(columnSize),0);
			StringBuilder temp = new StringBuilder();
			if(i == 0){
				temp.append("\r\n");
			}
			temp.append("\tprivate ");
			temp.append(javaType);
			temp.append(" ");
			temp.append(columnName);
			temp.append(";// ");
			temp.append(remarks);
			temp.append("\r\n");
			sb.append(temp);
		}
		log.info("结束创建私有属性");
		sb.append("\r\n");

		log.info("开始创建set、get方法");
		for (int i = 0; i < size; i++) {
			Map<String, Object> map = list.get(i);
			String columnName = columnNameToDeclareVar(map.get("columnName").toString());
			String columnType = map.get("columnType").toString();
			String columnSize = map.get("dataSize").toString();
			String javaType = oracleSqlType2JavaType(columnType.toLowerCase(),Integer.parseInt(columnSize),0);
			StringBuilder temp = new StringBuilder();
			temp.append("\tpublic ");
			temp.append(javaType);
			temp.append(" ");
			temp.append("get");
			temp.append(toFirstUpper(columnName));
			temp.append("(){\r\n");

			temp.append("\t\treturn ");
			temp.append(columnName);
			temp.append(";\r\n");

			temp.append("\t}\r\n\r\n");
			sb.append(temp);

			temp = new StringBuilder();
			temp.append("\tpublic void ");
			temp.append("set");
			temp.append(toFirstUpper(columnName));
			temp.append("(");
			temp.append(javaType);
			temp.append(" ");
			temp.append(columnName);
			temp.append("){\r\n");

			temp.append("\t\tthis.");
			temp.append(columnName);
			temp.append(" = ");
			temp.append(columnName);
			temp.append(";\r\n");

			if(i == (list.size() - 1)){
				temp.append("\t}\r\n");
			}else{
				temp.append("\t}\r\n\r\n");
			}

			sb.append(temp);
		}
		log.info("结束创建set、get方法");
		sb.append("}");

		String pathName = dir + File.separator + tableName + ".java";
		File file = new File(pathName);
		FileUtils.writeStringToFile(file, sb.toString(), Constants.CHARSET);
		long endTimeMillis = System.currentTimeMillis();
		log.info("结束创建java文件");
		log.info("创建耗时:" + (endTimeMillis - startTimeMillis) + "毫秒");
        return sb.toString();
    }
    
    /**
	 * 这里可以对javaBean进行类型转换 目前是String类型
	 * @param sqlType
	 * @param size
	 * @param scale
	 * @return
	 */
    private static String oracleSqlType2JavaType(String sqlType, int size, int scale){
        return "String";
    }
    
    /**
     * 首字母大写
     * @param str
     * @return
     */
	private static String toFirstUpper(String str) {
		if (StringUtils.isBlank(str)) {
			throw new NullPointerException();
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	/**
	 * 首字母小写
	 * @param str
	 * @return
	 */
	private static String toFirstLowwer(String str) {
		if (StringUtils.isBlank(str)) {
			throw new NullPointerException();
		}
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	/**
	 * 数据库字段名转驼峰样式
	 * @param columnName
	 * @return
	 */
	private static String columnNameToDeclareVar(String columnName) {
		columnName = StringUtils.removeStartIgnoreCase(columnName, "t_");
		String[] words = columnName.split("_");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < words.length; i++) {
			sb.append(toFirstUpper(words[i].toLowerCase()));
		}
		return sb.toString();
	}
}