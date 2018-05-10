package com.db4J.glb;

import javax.swing.*;
import java.util.List;

/**
 * @author: love720720@163.com
 * @date: 2018年04月27日 12时10分
 */
public class BaseBean {
    // 当前页
    private int pageIndex;
    // 开始页
    private int firstIndex;
    // 结束页
    private int lastIndex;
    // 检索内容
    private String retrievalVal;
    // 数据库类型
    private JComboBox<String> dbTypeBox;
    // 数据库
    private JComboBox<String> dbBox;
    // 包名
    private JComboBox<String> packBox;
    // 输出目录
    private JComboBox<String> dirBox;
    // 检索框
    private JTextField retrievalFielid;
    // 提示框
    private JTextArea hintArea;
    // 多选框
    private JCheckBox checkBox;
    // 表格
    private JTable tabLable;
    // 查询按钮
    private JButton searchButton;
    // 检索按钮
    private JButton retrievalButton;
    // 创建java按钮
    private JButton createJavaButton;
    // 创建sql按钮
    private JButton createSqlButton;
    // 分页-首页按钮
    private JButton homeButton;
    // 分页-末页按钮
    private JButton lastButton;
    // 分页-上一页按钮
    private JButton prevButton;
    // 分页-下一页
    private JButton nextButton;
    // 表格
    private TableModel tabModel;
    // 标题
    private String[] titles = {"选择", "表格名称"};
    // 提示信息
    private List<String> hintList;

    public List<String> getHintList() {
        return hintList;
    }

    public void setHintList(List<String> hintList) {
        this.hintList = hintList;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(int firstIndex) {
        this.firstIndex = firstIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public String getRetrievalVal() {
        return retrievalVal;
    }

    public void setRetrievalVal(String retrievalVal) {
        this.retrievalVal = retrievalVal;
    }

    public JComboBox<String> getDbTypeBox() {
        return dbTypeBox;
    }

    public void setDbTypeBox(JComboBox<String> dbTypeBox) {
        this.dbTypeBox = dbTypeBox;
    }

    public JComboBox<String> getDbBox() {
        return dbBox;
    }

    public void setDbBox(JComboBox<String> dbBox) {
        this.dbBox = dbBox;
    }

    public JComboBox<String> getPackBox() {
        return packBox;
    }

    public void setPackBox(JComboBox<String> packBox) {
        this.packBox = packBox;
    }

    public JComboBox<String> getDirBox() {
        return dirBox;
    }

    public void setDirBox(JComboBox<String> dirBox) {
        this.dirBox = dirBox;
    }

    public JTextField getRetrievalFielid() {
        return retrievalFielid;
    }

    public void setRetrievalFielid(JTextField retrievalFielid) {
        this.retrievalFielid = retrievalFielid;
    }

    public JTextArea getHintArea() {
        return hintArea;
    }

    public void setHintArea(JTextArea hintArea) {
        this.hintArea = hintArea;
    }

    public JCheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(JCheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public JTable getTabLable() {
        return tabLable;
    }

    public void setTabLable(JTable tabLable) {
        this.tabLable = tabLable;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public void setSearchButton(JButton searchButton) {
        this.searchButton = searchButton;
    }

    public JButton getRetrievalButton() {
        return retrievalButton;
    }

    public void setRetrievalButton(JButton retrievalButton) {
        this.retrievalButton = retrievalButton;
    }

    public JButton getCreateJavaButton() {
        return createJavaButton;
    }

    public void setCreateJavaButton(JButton createJavaButton) {
        this.createJavaButton = createJavaButton;
    }

    public JButton getCreateSqlButton() {
        return createSqlButton;
    }

    public void setCreateSqlButton(JButton createSqlButton) {
        this.createSqlButton = createSqlButton;
    }

    public JButton getHomeButton() {
        return homeButton;
    }

    public void setHomeButton(JButton homeButton) {
        this.homeButton = homeButton;
    }

    public JButton getLastButton() {
        return lastButton;
    }

    public void setLastButton(JButton lastButton) {
        this.lastButton = lastButton;
    }

    public JButton getPrevButton() {
        return prevButton;
    }

    public void setPrevButton(JButton prevButton) {
        this.prevButton = prevButton;
    }

    public JButton getNextButton() {
        return nextButton;
    }

    public void setNextButton(JButton nextButton) {
        this.nextButton = nextButton;
    }

    public TableModel getTabModel() {
        return tabModel;
    }

    public void setTabModel(TableModel tabModel) {
        this.tabModel = tabModel;
    }

    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }
}
