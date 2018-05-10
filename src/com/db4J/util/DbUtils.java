package com.db4J.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import com.db4J.exp.BaseException;
import com.db4J.glb.BaseBean;
import com.db4J.glb.Constants;
import com.db4J.init.Db4J;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 *
 * @author love720720@163.com
 * @date 2017年5月15日 下午3:35:09
 */
public class DbUtils {

	private static Logger log = LogUtils.getLogger();

	private static Connection connection = null;
	
	private static Statement statement = null;

	// 用户输入信息
	public static Map<String, String> userConfigMap;
	// 配置文件信息
	private static Map<String, HashMap<String, String>> dbConfigMap;

	/**
	 * 获取数据库链接
	 * @return
	 * @throws BaseException
	 * @throws Exception
	 */
	public static Connection getOracleConnection() throws BaseException, Exception {
		try {
			String driver = userConfigMap.get("driver");
			String user = userConfigMap.get("user");
			String pwd = userConfigMap.get("pwd");
			String jdbc = userConfigMap.get("jdbc");
			Class.forName(driver);
			Properties props = new Properties();
			props.put("user", user);
			props.put("password", pwd);
			props.put("remarksReporting", "true");
			connection = DriverManager.getConnection(jdbc, props);
			
			statement = connection.createStatement();
		} catch (Exception e) {
			throw new BaseException("获取数据库链接异常", e);
		}
		return connection;
	}

	/**
	 * 读取数据库表
	 * @return
	 * @throws Exception
	 * @param baseBean
	 */
	public static List<String> getTableNames(BaseBean baseBean) throws BaseException, Exception {
		String retrievalVal = baseBean.getRetrievalVal();
		int pageIndex = baseBean.getPageIndex();
		List<String> tableList = new ArrayList<String>(); // 存储表名
		try {
			connection = DbUtils.getOracleConnection();
			int totalCount = 0;
			String tableCountSql = userConfigMap.get("tableCount");
			String where = StringUtils.EMPTY;
			if (StringUtils.isNotBlank(retrievalVal)) {
				retrievalVal = retrievalVal.toUpperCase();
				where = " WHERE INSTR(T.TABLE_NAME, '" + retrievalVal + "') > 0";
			}
			tableCountSql = tableCountSql.replace("#{where}", where);
			ResultSet rs = statement.executeQuery(tableCountSql);
			while (rs.next()) {
				totalCount = rs.getInt(1);
			}
			Page page = PageUtils.getInstance(totalCount, PageUtils.DEFAULT_COUNT, pageIndex);
			pageIndex = page.getPageIndex();
			int totalPageCount = page.getTotalPageCount();
			baseBean.setPageIndex(pageIndex);
			baseBean.setFirstIndex(1);
			baseBean.setLastIndex(totalPageCount);

			int firstIndex = page.getIndex();
			int lastIndex = PageUtils.DEFAULT_COUNT * pageIndex;

			String tableListSql = userConfigMap.get("tableList");
			tableListSql = tableListSql.replace("#{where}", where);
			tableListSql = tableListSql.replace("#{firstIndex}", String.valueOf(firstIndex));
			tableListSql = tableListSql.replace("#{lastIndex}", String.valueOf(lastIndex));

			rs = statement.executeQuery(tableListSql);
			while (rs.next()) {
				tableList.add(rs.getString(1));
			}
		} catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException("读取数据库表异常", e);
        } finally {
			if (connection != null) {
				connection.close();
			}
			if (statement != null) {
				statement.close();
			}
		}
		return tableList;
	}

	/**
	 * 获取数据库表字段
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String,Object>> getColumnNames(String tableName) throws BaseException, Exception {
		if(StringUtils.isBlank(tableName)){
			throw new BaseException("tableName为空");
		}
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			connection = DbUtils.getOracleConnection();
			String user = userConfigMap.get("user");
			DatabaseMetaData dmd = connection.getMetaData();
			ResultSet rs = dmd.getColumns(null, StringUtils.upperCase(user, Locale.CHINA), tableName,"%");
			while (rs.next()) {
				String columnName = rs.getString("COLUMN_NAME");
				String columnType = rs.getString("TYPE_NAME");
				int dataSize = rs.getInt("COLUMN_SIZE");
				String remarks = rs.getString("REMARKS");
				remarks = remarks == null ? StringUtils.EMPTY : remarks;
				remarks = StringUtils.replaceAll(remarks, "\n", " ");
				Map<String,Object> map = new LinkedHashMap<String,Object>();
				map.put("columnName", columnName);
				map.put("columnType", columnType);
				map.put("dataSize", dataSize);
				map.put("remarks", remarks);
				list.add(map);

				log.info(tableName + "-->>columnName = " + columnName + "|remarks = " + remarks);
			}
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
			throw new BaseException("获取数据库表[" + tableName + "]字段异常", e);
		} finally {
			if (connection != null) {
				connection.close();
			}
			if (statement != null) {
				statement.close();
			}
		}
		return list;
	}

	/**
	 * 读取数据库配置
	 * @return
	 */
	public static Map<String, HashMap<String, String>> getDbConfigMap() throws BaseException, Exception {
		if (dbConfigMap != null) {
			return dbConfigMap;
		}
		dbConfigMap = new LinkedHashMap<String, HashMap<String,String>>();
		HashMap<String, String> itemsMap = null;
		InputStream is = null;
		BufferedReader reader = null;
		try {
			// 读取配置文件
			File file = new File(Constants.FILE_DB_CONFIG);
			
			is = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.length() <= 0){
					continue;
				}
				
				if (line.startsWith("[") && line.endsWith("]")) {
					itemsMap = new HashMap<String, String>();
					String dbName = line.substring(1, line.length() - 1);
					dbConfigMap.put(dbName, itemsMap);
					continue;
				}
				int index = line.indexOf("=");
				if (index != -1) {
					String key = line.substring(0, index);
					String value = line.substring(index + 1, line.length());
					itemsMap.put(key, value.trim());
				}
			}
		} catch (Exception e) {
			throw new BaseException("读取数据库配置异常", e);
		} finally {
			if (is != null) {
				is.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
		return dbConfigMap;
	}

	/**
	 * 读取用户配置文件
	 * @return
	 * @throws BaseException
	 * @throws Exception
	 */
	public static Map<String, String> getUserConfigMap() throws BaseException, Exception {
		if (userConfigMap != null) {
			return userConfigMap;
		}
		
		userConfigMap = new HashMap<String, String>();
		InputStream is = null;
		BufferedReader reader = null;
		try {
			// 读取配置文件
			File file = new File(Constants.FILE_UD_CONFIG);
			if (!file.exists()) {
				return userConfigMap;
			}
			
			// 存在配置文件
			is = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.length() <= 0){
					continue;
				}
				
				int index = line.indexOf("=");
				if (index != -1) {
					String key = line.substring(0, index);
					String value = line.substring(index + 1, line.length());
					userConfigMap.put(key, value.trim());
				}
			}
		} catch (Exception e) {
			throw new BaseException("读取用户配置文件异常", e);
		} finally {
			if (is != null) {
				is.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
		return userConfigMap;
	}
}