package com.db4J.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import com.db4J.glb.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 *
 * @author love720720@163.com
 * @date 2017年9月11日 下午4:53:11
 */
public class SqlUtils {

	private static Logger log = LogUtils.getLogger();
	
	/**
	 * 创建sql文件
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static String createFile(String tableName) throws Exception {
		log.info("开始创建sql文件");
        long startTimeMillis = System.currentTimeMillis();

		// 获取数据库字段
		List<Map<String, Object>> list = DbUtils.getColumnNames(tableName);
		// 获取文件路径
		String dir = DbUtils.getUserConfigMap().get("dirSql");

		String dbKey = null;
		String javaKey = null;

		// 创建新增sql
		log.info("开始创建新增sql");
		StringBuilder insert = new StringBuilder();
		insert.append("<insert id=\"insert\" parameterClass=\"insert\">");
		insert.append("\r\n");
		insert.append("	");
		insert.append("<selectKey resultClass=\"String\" keyProperty=\"id\">");
		insert.append("\r\n");
		insert.append("	");
		insert.append("	");
		insert.append("SELECT SE_XXX.NEXTVAL AS ID FROM DUAL");
		insert.append("\r\n");
		insert.append("	");
		insert.append("</selectKey>");
		insert.append("\r\n");
		insert.append("	");
		insert.append("INSERT INTO ");
		insert.append(tableName);
		insert.append("(");
		insert.append("\r\n");
		int size;
		for (int i = 0; i < (size = list.size()); i++) {
			Map<String, Object> map = list.get(i);
			String columnName = map.get("columnName").toString();
			if (i == 0) {
				dbKey = columnName;
				javaKey = columnNameToDeclareVar(columnName);
			}
			insert.append("	");
			insert.append("	");
			insert.append(columnName);
			if ((i + 1) != size) {
				insert.append(",");
			}
			insert.append("\r\n");
		}
		insert.append("	");
		insert.append(")VALUES(");
		insert.append("\r\n");
		for (int i = 0; i < size; i++) {
			Map<String, Object> map = list.get(i);
			String columnName = map.get("columnName").toString();
			String javaName = columnNameToDeclareVar(columnName);
			insert.append("	");
			insert.append("	");
			transDate(insert, javaName, columnName);
			if ((i + 1) != size) {
				insert.append(",");
			}
			insert.append("\r\n");
		}
		insert.append("	");
		insert.append(")");
		insert.append("\r\n");
		insert.append("</insert>");
		log.info("结束创建新增sql");

		// 创建删除sql
		log.info("开始创建删除sql");
		StringBuilder delete = new StringBuilder();
		delete.append("<delete id=\"delete\" parameterClass=\"delete\">");
		delete.append("\r\n");
		delete.append("	");
		delete.append("DELETE FROM");
		delete.append("\r\n");
		delete.append("	");
		delete.append("	");
		delete.append(tableName);
		delete.append(" T");
		delete.append("\r\n");
		delete.append("	");
		delete.append("WHERE");
		delete.append("\r\n");
		delete.append("	");
		delete.append("	");
		delete.append("T.");
		delete.append(dbKey);
		delete.append(" = ");
		delete.append("#");
		delete.append(javaKey);
		delete.append("#");
		delete.append("\r\n");
		delete.append("</delete>");
		log.info("结束创建删除sql");

		// 创建修改sql
		log.info("开始创建修改sql");
		StringBuilder update = new StringBuilder();
		update.append("<update id=\"update\" parameterClass=\"update\">");
		update.append("\r\n");
		update.append("	");
		update.append("UPDATE");
		update.append("\r\n");
		update.append("	");
		update.append("	");
		update.append(tableName);
		update.append(" T");
		update.append("\r\n");
		update.append("	");
		update.append("SET");
		update.append("\r\n");
		for (int i = 0; i < size; i++) {
			Map<String, Object> map = list.get(i);
			String columnName = map.get("columnName").toString();
			String javaName = columnNameToDeclareVar(columnName);
			update.append("	");
			update.append("	");
			update.append("T.");
			update.append(columnName);
			update.append(" = ");
			transDate(update, javaName, columnName);
			if ((i + 1) != size) {
				update.append(",");
			}
			update.append("\r\n");
		}
		update.append("	");
		update.append("WHERE");
		update.append("\r\n");
		update.append("	");
		update.append("	");
		update.append("T.");
		update.append(dbKey);
		update.append(" = ");
		update.append("#");
		update.append(javaKey);
		update.append("#");
		update.append("\r\n");
		update.append("</update>");
		log.info("结束创建修改sql");

		// 创建查询sql
		log.info("开始创建查询sql");
		StringBuilder select = new StringBuilder();
		select.append("<select id=\"select\" parameterClass=\"String\" resultClass=\"select\">");
		select.append("\r\n");
		select.append("	");
		select.append("SELECT");
		select.append("\r\n");

		for (int i = 0; i < size; i++) {
			Map<String, Object> map = list.get(i);
			String columnName = map.get("columnName").toString();
			String javaName = columnNameToDeclareVar(columnName);
			select.append("	");
			select.append("	");
			select.append("T.");
			select.append(columnName);
			select.append(" ");
			select.append(javaName);
			if ((i + 1) != size) {
				select.append(",");
			}
			select.append("\r\n");
		}
		select.append("	");
		select.append("FROM");
		select.append("\r\n");
		select.append("	");
		select.append("	");
		select.append(tableName);
		select.append(" T");
		select.append("\r\n");
		select.append("	");
		select.append("WHERE");
		select.append("\r\n");
		select.append("	");
		select.append("	");
		select.append("T.");
		select.append(dbKey);
		select.append(" = ");
		select.append("#");
		select.append(javaKey);
		select.append("#");
		select.append("\r\n");
		select.append("</select>");
		log.info("结束创建查询sql");

        StringBuffer sb = new  StringBuffer();
		sb.append("<!-- 新增 -->");
		sb.append("\r\n");
		sb.append(insert);
		sb.append("\r\n");
		sb.append("\r\n");
		sb.append("\r\n");

		sb.append("<!-- 删除 -->");
		sb.append("\r\n");
		sb.append(delete);
		sb.append("\r\n");
		sb.append("\r\n");
		sb.append("\r\n");

		sb.append("<!-- 修改 -->");
		sb.append("\r\n");
		sb.append(update);
		sb.append("\r\n");
		sb.append("\r\n");
		sb.append("\r\n");

		sb.append("<!-- 查询 -->");
		sb.append("\r\n");
		sb.append(select);
		sb.append("\r\n");

		String pathName = dir + File.separator + tableName + ".sql";
		File file = new File(pathName);
		FileUtils.writeStringToFile(file, sb.toString(), Constants.CHARSET);
        long endTimeMillis = System.currentTimeMillis();
		log.info("结束创建sql文件");
        log.info("创建耗时:" + (endTimeMillis - startTimeMillis) + "毫秒");
        return sb.toString();
    }

	/**
	 * 转换时间字段
	 * @param columnName
	 * @return
	 */
	private static void transDate(StringBuilder sb, String columnName, String javaName) {
		if (StringUtils.endsWith(columnName, "DATE") || StringUtils.endsWith(columnName, "TIME")) {
			// 遇到数据库字段以DATE、TIME结尾的使用系统时间
			sb.append("SYSDATE");
		} else {
			sb.append("#");
			sb.append(javaName);
			sb.append("#");
		}
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
		String[] words = columnName.split("_");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < words.length; i++) {
            sb.append(toFirstUpper(words[i].toLowerCase()));
		}
		return toFirstLowwer(sb.toString());
	}
}