package com.db4J.init;

import com.db4J.exp.BaseException;
import com.db4J.glb.BaseBean;
import com.db4J.glb.Constants;
import com.db4J.glb.TableModel;
import com.db4J.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 借鉴：https://www.oschina.net/code/snippet_128886_26726
 * 入口程序
 * @author love720720@163.com
 * @date 2017年5月15日 下午3:35:09
 */
public class Db4J extends JFrame {

	private static final long serialVersionUID = -1400866733239947205L;

    private static Logger log = LogUtils.getLogger();

    private void init(BaseBean baseBean) throws BaseException, Exception {
        setTitle("Java数据库工具Beta1.0");
        JPanel panel = new JPanel();
        // 初始化面板
        initPanel(baseBean, panel);
        // 初始化配置
        initData(baseBean);
    }

    /**
     * 初始化配置
     * @param baseBean
     */
    private void initData(BaseBean baseBean) throws BaseException, Exception {
        LogUtils.addHint(baseBean, "开始初始化配置");
        JComboBox<String> dbTypeBox = baseBean.getDbTypeBox();
        JComboBox<String> dbBox = baseBean.getDbBox();
        JComboBox<String> packBox = baseBean.getPackBox();
        JComboBox<String> dirBox = baseBean.getDirBox();

        Map<String, HashMap<String, String>> dbConfigMap = DbUtils.getDbConfigMap();
        Map<String, String> userConfigMap = DbUtils.getUserConfigMap();

        for (String key : dbConfigMap.keySet()) {
            dbTypeBox.addItem(key);
        }

        String list = userConfigMap.get("db");
        String[] split = StringUtils.split(list, ",");
        for (int i = 0; i < split.length; i++) {
            dbBox.addItem(split[i]);
        }

        String pack = userConfigMap.get("pack");
        split = StringUtils.split(pack, ",");
        for (int i = 0; i < split.length; i++) {
            packBox.addItem(split[i]);
        }
        
        String dir = userConfigMap.get("dir");
        split = StringUtils.split(dir, ",");
        for (int i = 0; i < split.length; i++) {
            dirBox.addItem(split[i]);
        }
        LogUtils.addHint(baseBean, "结束初始化配置");
    }

    /**
     * 初始化面板
     * @param baseBean
     * @param panel
     */
    private void initPanel(final BaseBean baseBean, JPanel panel) {
        LogUtils.addHint(baseBean, "开始初始化面板");
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);
        setResizable(false);

        JLabel dbTypeLabel = new JLabel("数据库类型：");
        dbTypeLabel.setBounds(60, 20, 80, 30);
        panel.add(dbTypeLabel);

        JComboBox dbTypeBox = new JComboBox<String>();
        dbTypeBox.setBounds(146, 20, 170, 30);
        dbTypeBox.setVisible(true);
        dbTypeBox.setMaximumRowCount(5);
        panel.add(dbTypeBox);
        baseBean.setDbTypeBox(dbTypeBox);

        JLabel dbLable = new JLabel("数据库配置：");
        dbLable.setBounds(60, 65, 80, 30);
        panel.add(dbLable);

        JComboBox dbBox = new JComboBox<String>();
        dbBox.setBounds(146, 65, 170, 30);
        dbBox.setVisible(true);
        dbBox.setMaximumRowCount(5);
        panel.add(dbBox);
        baseBean.setDbBox(dbBox);

        JLabel packLabel = new JLabel("工程包名：");
        packLabel.setBounds(73, 110, 80, 30);
        panel.add(packLabel);

        JComboBox packBox = new JComboBox<String>();
        packBox.setBounds(146, 110, 170, 30);
        packBox.setVisible(true);
        packBox.setMaximumRowCount(5);
        panel.add(packBox);
        baseBean.setPackBox(packBox);

        JLabel pathLabel = new JLabel("输出目录：");
        pathLabel.setBounds(73, 155, 80, 30);
        panel.add(pathLabel);

        JComboBox dirBox= new JComboBox<String>();
        dirBox.setBounds(146, 155, 170, 30);
        dirBox.setVisible(true);
        dirBox.setMaximumRowCount(5);
        panel.add(dirBox);
        baseBean.setDirBox(dirBox);

        JCheckBox checkBox = new JCheckBox("创建包结构目录");
        checkBox.setSelected(false);
        checkBox.setBounds(142, 190, 170, 30);
        panel.add(checkBox);
        baseBean.setCheckBox(checkBox);

        JButton searchButton = new JButton("查询");
        searchButton.setBounds(90, 225, 100, 28);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search(baseBean);
            }
        });
        panel.add(searchButton);
        baseBean.setSearchButton(searchButton);

        JButton createJavaButton = new JButton("创建java文件");
        createJavaButton.setBounds(205, 225, 110, 28);
        createJavaButton.setEnabled(false);
        createJavaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createJava(baseBean);
            }
        });
        panel.add(createJavaButton);
        baseBean.setCreateJavaButton(createJavaButton);

        JButton createSqlButton = new JButton("创建sql文件 ");
        createSqlButton.setBounds(205, 265, 110, 28);
        createSqlButton.setEnabled(false);
        createSqlButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createSql(baseBean);
            }
        });
        panel.add(createSqlButton);
        baseBean.setCreateSqlButton(createSqlButton);

        // 初始化检索按钮
        JButton retrievalButton = new JButton("检索");
        retrievalButton.setEnabled(false);
        // 按钮增加动作执行retrieve()方法
        retrievalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                retrieval(baseBean);
            }
        });
        retrievalButton.setBounds(370, 20, 75, 30);
        panel.add(retrievalButton);
        baseBean.setRetrievalButton(retrievalButton);

        JTextField retrievalFielid = new JTextField();
        retrievalFielid.setBounds(460, 20, 310, 30);
        panel.add(retrievalFielid);
        baseBean.setRetrievalFielid(retrievalFielid);

        // 设置表格
        String[] titles = baseBean.getTitles();
        Object[][] tabData = {};
        TableModel tabModel = new TableModel(tabData, titles);
        final JTable tabLable = new JTable(tabModel);
        tabLable.setRowHeight(20);
        tabLable.getColumnModel().getColumn(0).setPreferredWidth(50);//设置列宽
        tabLable.getColumnModel().getColumn(1).setPreferredWidth(350);//设置列宽
        JScrollPane scr = new JScrollPane(tabLable);
        scr.setBounds(370, 60, 400, 425);
        panel.add(scr);
        tabLable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (tabLable.getSelectedColumn() == 0) {
                    // 如果是第一列的单元格，则返回，不响应点击
                    return;
                }
            }
        });
        baseBean.setTabLable(tabLable);
        baseBean.setTabModel(tabModel);

        JLabel logLabel = new JLabel("日志输出：");
        logLabel.setBounds(20, 280, 80, 30);
        panel.add(logLabel);

        // 提示信息
        JTextArea hintArea = new JTextArea();
        //hintArea.setForeground(Color.BLACK);
        hintArea.setEnabled(false);
        hintArea.setLineWrap(true);
        hintArea.setWrapStyleWord(true);

        // 是为了让JTextArea显示滚动条
        JScrollPane scroll = new JScrollPane(hintArea);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setBounds(20, 310, 330, 220);
        panel.add(scroll);
        baseBean.setHintArea(hintArea);

        // 初始化分页按钮
        JButton homeButton = new JButton("首页");
        homeButton.setEnabled(false);
        // 按钮增加动作执行toHome()方法
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clickHomeButton(baseBean);
            }
        });
        homeButton.setBounds(415, 505, 75, 25);
        panel.add(homeButton);
        baseBean.setHomeButton(homeButton);

        JButton prevButton = new JButton("上一页");
        prevButton.setEnabled(false);
        // 按钮增加动作执行toHome()方法
        prevButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clickPrevButton(baseBean);
            }
        });

        prevButton.setBounds(495, 505, 75, 25);
        panel.add(prevButton);
        baseBean.setPrevButton(prevButton);

        JButton nextButton = new JButton("下一页");
        nextButton.setEnabled(false);
        // 按钮增加动作执行toHome()方法
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clickNextButton(baseBean);
            }
        });

        nextButton.setBounds(575, 505, 75, 25);
        panel.add(nextButton);
        baseBean.setNextButton(nextButton);

        JButton lastButton = new JButton("末页");
        lastButton.setEnabled(false);
        // 按钮增加动作执行toHome()方法
        lastButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clickLastButton(baseBean);
            }
        });

        lastButton.setBounds(655, 505, 75, 25);
        panel.add(lastButton);
        baseBean.setLastButton(lastButton);

        // 增加关闭事件监听，关闭相关操作
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                close();
            }
        });
        LogUtils.addHint(baseBean, "结束初始化面板");
    }

    /**
     * 点击末页按钮
     * @param baseBean
     */
    private void clickLastButton(BaseBean baseBean) {
        JButton homeButton = baseBean.getHomeButton();
        JButton prevButton = baseBean.getPrevButton();
        JButton nextButton = baseBean.getNextButton();
        JButton lastButton = baseBean.getLastButton();
        int lastIndex = baseBean.getLastIndex();

        lastButton.setEnabled(false);
        nextButton.setEnabled(false);
        if (lastIndex > 1) {
            homeButton.setEnabled(true);
            prevButton.setEnabled(true);
        } else {
            prevButton.setEnabled(false);
        }
        baseBean.setPageIndex(lastIndex);
        list(baseBean);
    }

    /**
     * 点击下一页按钮
     * @param baseBean
     */
    private void clickNextButton(BaseBean baseBean) {
        JButton homeButton = baseBean.getHomeButton();
        JButton prevButton = baseBean.getPrevButton();
        JButton nextButton = baseBean.getNextButton();
        JButton lastButton = baseBean.getLastButton();
        int pageIndex = baseBean.getPageIndex();
        int lastIndex = baseBean.getLastIndex();

        prevButton.setEnabled(true);
        homeButton.setEnabled(true);
        if (++pageIndex >= lastIndex) {
            lastButton.setEnabled(false);
            nextButton.setEnabled(false);
        } else {
            lastButton.setEnabled(true);
            nextButton.setEnabled(true);
        }
        baseBean.setPageIndex(pageIndex);
        list(baseBean);
    }

    /**
     * 点击上一页按钮
     * @param baseBean
     */
    private void clickPrevButton(BaseBean baseBean) {
        JButton homeButton = baseBean.getHomeButton();
        JButton prevButton = baseBean.getPrevButton();
        JButton nextButton = baseBean.getNextButton();
        JButton lastButton = baseBean.getLastButton();
        int pageIndex = baseBean.getPageIndex();
        int firstIndex = baseBean.getFirstIndex();

        nextButton.setEnabled(true);
        lastButton.setEnabled(true);
        if (--pageIndex <= firstIndex) {
            homeButton.setEnabled(false);
            prevButton.setEnabled(false);
        } else {
            homeButton.setEnabled(true);
            prevButton.setEnabled(true);
        }
        baseBean.setPageIndex(pageIndex);
        list(baseBean);
    }

    /**
     * 点击首页按钮
     * @param baseBean
     */
    private void clickHomeButton(BaseBean baseBean) {
        JButton homeButton = baseBean.getHomeButton();
        JButton prevButton = baseBean.getPrevButton();
        JButton nextButton = baseBean.getNextButton();
        JButton lastButton = baseBean.getLastButton();
        int firstIndex = baseBean.getFirstIndex();
        int lastIndex = baseBean.getLastIndex();

        homeButton.setEnabled(false);
        prevButton.setEnabled(false);
        if (lastIndex > 1) {
            lastButton.setEnabled(true);
            nextButton.setEnabled(true);
        } else {
            nextButton.setEnabled(false);
        }
        baseBean.setPageIndex(firstIndex);
        list(baseBean);
    }

    /**
     * 检索
     * @param baseBean
     */
    private void retrieval(BaseBean baseBean) {
        JTextField retrievalFielid = baseBean.getRetrievalFielid();
        JButton retrievalButton = baseBean.getRetrievalButton();
        String retrievalVal = retrievalFielid.getText();
        baseBean.setRetrievalVal(retrievalVal);
        baseBean.setPageIndex(0);
        restButton(baseBean);
        list(baseBean);
        retrievalButton.setEnabled(true);
    }

    /**
     * 查询
     * @param baseBean
     */
    private void search(BaseBean baseBean) {
        try {
            JTextField retrievalFielid = baseBean.getRetrievalFielid();
            retrievalFielid.setText(StringUtils.EMPTY);
            baseBean.setRetrievalVal(StringUtils.EMPTY);
            baseBean.setPageIndex(0);
            restButton(baseBean);
            initConfig(baseBean);
            list(baseBean);
        } catch (BaseException e) {
            String msg = e.getLocalizedMessage();
            log.error(msg, e);
            LogUtils.addHint(baseBean, msg);
        } catch (Exception e) {
            String msg = e.getLocalizedMessage();
            log.error(msg, e);
            LogUtils.addHint(baseBean, "系统异常");
        } finally {
            printLog(baseBean);
        }
    }

    /**
	 * 设置居中
	 * @param width
	 * @param height
	 */
	private void setSizeAndCentralizeMe(int width, int height) {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = defaultToolkit.getScreenSize();
		this.setSize(width, height);
		this.setLocation(screenSize.width / 2 - width / 2, screenSize.height / 2 - height / 2);
	}

	/**
	 * 创建java文件
	 */
	private void createJava(BaseBean baseBean) {
        try {
            long startMillis = System.currentTimeMillis();
            // 是否勾选创建目录标识
            JCheckBox checkBox = baseBean.getCheckBox();
            boolean isSelected = checkBox.isSelected();
            // 输入的本地路径 默认安装路
            Map<String, String> userConfigMap = DbUtils.getUserConfigMap();
            String dir = userConfigMap.get("dir");
            String pack = userConfigMap.get("pack");
            dir = dir + File.separator + Constants.DIR_OUT_JAVA;

            if (isSelected) {
                if (StringUtils.isNotBlank(pack)) {
                    pack = pack.replace(".", File.separator);
                    dir += pack;
                }
            }

            // 存入路径
            userConfigMap.put("dirJava", dir);
            // 创建java文件
            createJavaFile(baseBean);

            long endMillis = System.currentTimeMillis();
            LogUtils.addHint(baseBean, "java文件创建成功 耗时" + (endMillis - startMillis) + "毫秒");
        } catch (BaseException e) {
            String msg = e.getLocalizedMessage();
            log.error(msg, e);
            LogUtils.addHint(baseBean, msg);
        } catch (Exception e) {
            String msg = e.getLocalizedMessage();
            log.error(msg, e);
            LogUtils.addHint(baseBean, "系统异常");
        } finally {
            printLog(baseBean);
        }
	}

    /**
     * 打印日志
     * @param baseBean
     */
    private static void printLog(BaseBean baseBean) {
        JTextArea hintArea = baseBean.getHintArea();
        if (hintArea == null) {
            return;
        }
        List<String> hintList = baseBean.getHintList();
        if (hintList == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String hint : hintList) {
            sb.append(hint).append("\n");
        }
        hintArea.setText(sb.toString());
    }

    /**
	 * 创建sql文件
	 * @throws BaseException
	 * @throws Exception
	 */
	private void createSql(BaseBean baseBean) {
        try {
            long startMillis = System.currentTimeMillis();
            // 是否勾选创建目录标识
            JCheckBox checkBox = baseBean.getCheckBox();
            boolean isSelected = checkBox.isSelected();
            // 输入的本地路径 默认安装路
            Map<String, String> userConfigMap = DbUtils.getUserConfigMap();
            String dir = userConfigMap.get("dir");
            String pack = userConfigMap.get("pack");
            dir = dir + File.separator + Constants.DIR_OUT_SQL;

            if (isSelected) {
                if (StringUtils.isNotBlank(pack)) {
                    pack = pack.replace(".", File.separator);
                    dir += pack;
                }
            }
            // 存入路径
            userConfigMap.put("dirSql", dir);
            // 创建sql文件
            createSqlFile(baseBean);

            long endMillis = System.currentTimeMillis();
            LogUtils.addHint(baseBean, "sql文件创建成功 耗时" + (endMillis - startMillis) + "毫秒");
        } catch (BaseException e) {
            String msg = e.getLocalizedMessage();
            log.error(msg, e);
            LogUtils.addHint(baseBean, msg);
        } catch (Exception e) {
            String msg = e.getLocalizedMessage();
            log.error(msg, e);
            LogUtils.addHint(baseBean, "系统异常");
        } finally {
            printLog(baseBean);
        }
	}

    /**
     * 创建sql文件
     * @param baseBean
     * @throws BaseException
     * @throws Exception
     */
	private void createSqlFile(BaseBean baseBean) throws BaseException, Exception {
        Map<String, String> userConfigMap = DbUtils.getUserConfigMap();
        String dir = userConfigMap.get("dirSql");
        FileUtils.mkdirs(dir);

        JTable tabLable = baseBean.getTabLable();
        int rowCount = tabLable.getRowCount();
        int count = 0;
        for (int i = 0; i < rowCount; i++) {
            Object valueAt = tabLable.getValueAt(i, 0);
            if (valueAt == null) {
                continue;
            }
            if (valueAt.toString().equals("true")) {
                String tableName = tabLable.getValueAt(i, 1).toString();
                SqlUtils.createFile(tableName);
                count++;
            }
        }
        if (count <= 0) {
            throw new BaseException("请先勾选需要创建sql文件的数据库表");
        }
        LogUtils.addHint(baseBean, "输出目录" + dir);
    }

    /**
     * 创建java文件
     * @param baseBean
     * @throws BaseException
     * @throws Exception
     */
	private void createJavaFile(BaseBean baseBean) throws BaseException, Exception {
        Map<String, String> userConfigMap = DbUtils.getUserConfigMap();
        String dir = userConfigMap.get("dirJava");
        FileUtils.mkdirs(dir);

        JTable tabLable = baseBean.getTabLable();
        int rowCount = tabLable.getRowCount();
        int count = 0;
        for (int i = 0; i < rowCount; i++) {
            Object valueAt = tabLable.getValueAt(i, 0);
            if (valueAt == null) {
                continue;
            }
            if (valueAt.toString().equals("true")) {
                String tableName = tabLable.getValueAt(i, 1).toString();
                JavaUtils.createFile(tableName);
                count++;
            }
        }
        if (count <= 0) {
            throw new BaseException("请先勾选需要创建java文件的数据库表");
        }
        LogUtils.addHint(baseBean, "输出目录" + dir);
    }

	/**
	 * 集合数据
     * @param baseBean
	 */
	private void list(BaseBean baseBean) {
        try {
            long startMillis = System.currentTimeMillis();
            TableModel tabModel = baseBean.getTabModel();
            JButton retrievalButton = baseBean.getRetrievalButton();
            JButton createJavaButton = baseBean.getCreateJavaButton();
            JButton createSqlButton = baseBean.getCreateSqlButton();
            JButton lastButton = baseBean.getLastButton();
            JButton nextButton = baseBean.getNextButton();

            removeTable(baseBean);

            // 获取表名
            List<String> tabList = DbUtils.getTableNames(baseBean);
			if (tabList.size() > 0) {
                for (String tabName : tabList) {
                    Object[] rowData = { new Boolean(false), tabName };
                    tabModel.addRow(rowData);
                }

                int pageIndex = baseBean.getPageIndex();
                int lastIndex = baseBean.getLastIndex();
                retrievalButton.setEnabled(true);
                createJavaButton.setEnabled(true);
                createSqlButton.setEnabled(true);
                if (lastIndex > pageIndex) {
                    lastButton.setEnabled(true);
                    nextButton.setEnabled(true);
                }
            } else {
			    restButton(baseBean);
            }
            long endMillis = System.currentTimeMillis();
            LogUtils.addHint(baseBean, "查询成功 耗时" + (endMillis - startMillis) + "毫秒");
		} catch (BaseException e) {
            restButton(baseBean);
            String msg = e.getLocalizedMessage();
            log.error(msg, e);
            LogUtils.addHint(baseBean, msg);
        } catch (Exception e) {
            restButton(baseBean);
            String msg = e.getLocalizedMessage();
            log.error(msg, e);
            LogUtils.addHint(baseBean, "系统异常");
        } finally {
            printLog(baseBean);
        }
	}

    /**
     * 重置按钮
     * @param baseBean
     */
    private void restButton(BaseBean baseBean) {
        JButton retrievalButton = baseBean.getRetrievalButton();
        JButton createJavaButton = baseBean.getCreateJavaButton();
        JButton createSqlButton = baseBean.getCreateSqlButton();
        JButton homeButton = baseBean.getHomeButton();
        JButton lastButton = baseBean.getLastButton();
        JButton prevButton = baseBean.getPrevButton();
        JButton nextButton = baseBean.getNextButton();

        retrievalButton.setEnabled(false);
        createJavaButton.setEnabled(false);
        createSqlButton.setEnabled(false);
        homeButton.setEnabled(false);
        lastButton.setEnabled(false);
        prevButton.setEnabled(false);
        nextButton.setEnabled(false);

        removeTable(baseBean);
    }

    /**
     * 删除table数据
     */
    private void removeTable(BaseBean baseBean) {
        TableModel tabModel = baseBean.getTabModel();
        int rowCount = tabModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            tabModel.removeRow(0);
        }
    }

    /**
	 * 初始化配置文件
	 * @throws BaseException
	 * @throws Exception
	 */
    private void initConfig(BaseBean baseBean) throws BaseException,  Exception {
        JComboBox<String> dbTypeBox = baseBean.getDbTypeBox();
        JComboBox<String> dbBox = baseBean.getDbBox();
        JComboBox<String> packBox = baseBean.getPackBox();
        JComboBox<String> dirBox = baseBean.getDirBox();

        Object selectedItem = dbTypeBox.getSelectedItem();
		if (selectedItem == null) {
			throw new BaseException("请选择数据库类型");
		}
		String dbType = selectedItem.toString();

		selectedItem = dbBox.getSelectedItem();
		if (selectedItem == null) {
			throw new BaseException("请选择数据库配置");
		}
		String db = selectedItem.toString();

		selectedItem = packBox.getSelectedItem();
		if (selectedItem == null) {
			throw new BaseException("请选择工程包名");
		}
		String pack = selectedItem.toString();
		String dir = Constants.DIR_USER;
		selectedItem = dirBox.getSelectedItem();
		if (selectedItem != null) {
            dir = selectedItem.toString();
		}

		Map<String, HashMap<String, String>> dbConfigMap = DbUtils.getDbConfigMap();
		Map<String, String> userConfigMap = DbUtils.getUserConfigMap();

		String dbCfg = userConfigMap.get(db);
		String user = userConfigMap.get(db + "_user");
		String pwd = userConfigMap.get(db + "_pwd");

		HashMap<String, String> itemsMap = dbConfigMap.get(dbType);
		String jdbc = itemsMap.get("jdbc");
		String driver = itemsMap.get("driver");
		String separator = itemsMap.get("separator");
		String tableCount = itemsMap.get("tableCount");
		String tableList = itemsMap.get("tableList");
		jdbc = jdbc + dbCfg;

		String[] split = dbCfg.split(separator);
		String ip = split[0];
		String sid = split[2];

		// 处理界面数据
		userConfigMap.put("ip", ip);
		userConfigMap.put("sid", sid);
		userConfigMap.put("user", user);
		userConfigMap.put("pwd", pwd);
		userConfigMap.put("jdbc", jdbc);
		userConfigMap.put("driver", driver);
		userConfigMap.put("separator", separator);
		userConfigMap.put("db", db);
		userConfigMap.put("pack", pack);
		userConfigMap.put("dir", dir);
		userConfigMap.put("tableCount", tableCount);
		userConfigMap.put("tableList", tableList);
	}

	/**
	 * 窗口关闭
	 */
	private void close() {
        log.info("窗口关闭");
		System.exit(0);
	}

    /**
	 * 初始化字体
     * @param baseBean
     */
    private static void initFont(BaseBean baseBean) {
        Font font = new Font("微软雅黑", Font.PLAIN, 13);
		FontUIResource fontRes = new FontUIResource(font);
        UIDefaults defaults = UIManager.getDefaults();
        Enumeration<Object> keys = defaults.keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
        LogUtils.addHint(baseBean, "初始化字体成功");
	}

    /**
     * 初始化db4J
     * @param baseBean
     */
    private static void initDb4J(BaseBean baseBean) throws BaseException, Exception {
        LogUtils.addHint(baseBean, "开始初始化窗体");
        // 创建对象
        Db4J db4J = new Db4J();
        // 初始化
        db4J.init(baseBean);
        // 设置可见
        db4J.setVisible(true);
        // 点击X关闭窗口
        db4J.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 调用设置居中显示
        db4J.setSizeAndCentralizeMe(800, 580);
        LogUtils.addHint(baseBean, "结束初始化窗体");
    }

	/**
	 * 入口
	 * @param args
	 */
	public static void main(String[] args) {
        BaseBean baseBean = new BaseBean();
        try {
            // 初始化log
            LogUtils.initLog(baseBean);
            // 初始化字体
            initFont(baseBean);
            // 初始化db4J
            initDb4J(baseBean);
		} catch (BaseException e) {
			String msg = e.getLocalizedMessage();
            log.error(msg, e);
            LogUtils.addHint(baseBean, msg);
            e.printStackTrace();
		} catch (Exception e) {
			String msg = e.getLocalizedMessage();
            log.error(msg, e);
            LogUtils.addHint(baseBean, "系统异常");
            e.printStackTrace();
		} finally {
            printLog(baseBean);
        }
	}
}