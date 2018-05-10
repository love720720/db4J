package com.db4J.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** 
 *
 * @author love720720@163.com
 * @date 2017年6月14日 下午3:23:27
 */
public class PageUtils {

	public final static int DEFAULT_COUNT = 20;
	public final static int DEFAULT_PAGE_NUM = 9;
	public final static int DEFAULT_PAGE_INDEX = 1;

    /**
     * 分页
     * @param totalCount 总条数
     */
    public static Page getInstance(int totalCount) {
		return getInstance(totalCount, DEFAULT_COUNT, DEFAULT_PAGE_INDEX, DEFAULT_PAGE_NUM);
    }
    
    /**
     * 分页
     * @param totalCount 总条数
     * @param count 每页显示多少条数
     */
    public static Page getInstance(int totalCount,int count) {
		return getInstance(totalCount, count, DEFAULT_PAGE_INDEX, DEFAULT_PAGE_NUM);
    }
    /**
     * 分页
     * @param totalCount 总条数
     * @param count 每页显示多少条数
     * @param pageIndex 当前页
     */
    public static Page getInstance(int totalCount,int count,int pageIndex) {
		return getInstance(totalCount, count, pageIndex, DEFAULT_PAGE_NUM);
    }
    
    /**
     * 分页
     * @param totalCount 总条数
     * @param count 每页显示多少条数
     * @param pageIndex 当前页
     * @param pageNum 显示页码 默认分页为9个页码
     */
	public static Page getInstance(int totalCount, int count, int pageIndex, int pageNum) {
		Page page = new Page();
		pageIndex = pageIndex <= 0 ? DEFAULT_PAGE_INDEX : pageIndex;
		pageNum = pageNum <= 0 ? DEFAULT_PAGE_NUM : pageNum;
		page.setPageIndex(pageIndex);
		page.setCount(count);
		page.setIndex((pageIndex - 1) * count);
		page.setTotalCount(totalCount);
		setTotalCount(page);
		int totalPageCount = page.getTotalPageCount();
		int firstIndex = (pageIndex <= pageNum / 2 + 1 ? 1 : (pageIndex-pageNum / 2));
		int lastIndex = (firstIndex + pageNum-1 >= totalPageCount ? totalPageCount : firstIndex + pageNum - 1);
		int temp = lastIndex - firstIndex + 1;
		temp = temp == pageNum ? 0 : pageNum - temp;
		temp = temp < 0 ? 0 : temp;
		firstIndex = firstIndex - temp;
		firstIndex = firstIndex <= 0 ? 1 : firstIndex;
		lastIndex = lastIndex > totalPageCount ? totalPageCount : lastIndex;
		firstIndex = firstIndex > lastIndex ? lastIndex : firstIndex;
		page.setFirstIndex(firstIndex);
		page.setLastIndex(lastIndex);
		return page;
	}
    
	private static void setTotalCount(Page page) {
		int count = page.getCount();
		int totalCount = page.getTotalCount();
		int totalPageCount = page.getTotalPageCount();
		if(count > 0) {
			totalPageCount = totalCount / count;
			if(totalCount % count != 0) {
				totalPageCount++;
			}
		}
		page.setTotalPageCount(totalPageCount);
	}
}

class Page {
	private int index;//下标 从第几个开始
	private int totalCount;//总条数
	private int totalPageCount;//总页数
	private int count;//当前页显示条数
	private int pageIndex;//当前页
	private int firstIndex;//开始页
	private int lastIndex;//结束页

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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
}