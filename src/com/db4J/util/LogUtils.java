package com.db4J.util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import com.db4J.glb.BaseBean;
import com.db4J.glb.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author love720720@163.com
 * @date 2017年5月8日 下午4:15:41
 */
public class LogUtils {

	/**
	 * 获取logger
	 * @return
	 */
	public static Logger getLogger(){
		Thread thread = Thread.currentThread();
		StackTraceElement[] stackTrace = thread.getStackTrace();
		String className = stackTrace[2].getClassName();
		return LoggerFactory.getLogger(className);
	}

	/**
	 * 初始化logBack
	 * @param baseBean
	 */
	public static void initLog(BaseBean baseBean) throws Exception {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(lc);
		lc.reset();
		configurator.doConfigure(Constants.FILE_LOG_CONFIG);
		StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
		addHint(baseBean, "初始化log成功");
	}

	/**
	 * 添加提示信息
	 * @param baseBean
	 * @param hint
	 */
	public static void addHint(BaseBean baseBean, String hint) {
		if (StringUtils.isBlank(hint)) {
			return;
		}
		List<String> hintList = baseBean.getHintList();
		if (hintList == null) {
			hintList = new ArrayList<String>();
			baseBean.setHintList(hintList);
		}
		if (hintList.size() > 1000) {
			hintList.clear();
		}
		hint = DateUtils.getCurrentDate() + ": " + hint;
		hintList.add(hint);
	}
}