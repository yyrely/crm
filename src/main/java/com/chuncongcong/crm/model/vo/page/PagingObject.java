package com.chuncongcong.crm.model.vo.page;

import java.io.Serializable;
import java.util.List;

/**
 * @author HU
 * @date 2021/3/5 11:50
 */

public interface PagingObject<T> extends Iterable<T>, Serializable {

	/**
	 * 页码
	 *
	 * @return
	 */
	long getPageNum();

	/**
	 * 分页大小
	 *
	 * @return
	 */
	long getPageSize();

	/**
	 * 总页数
	 *
	 * @return
	 */
	int getTotalPages();

	/**
	 * 总记录数
	 *
	 * @return
	 */
	long getTotalElements();

	/**
	 * 分页内容记录数
	 *
	 * @return
	 */
	int getNumberOfElements();

	/**
	 * 是否有上一页
	 *
	 * @return
	 */
	boolean hasPrevious();

	/**
	 * 是否有下一页
	 *
	 * @return
	 */
	boolean hasNext();

	/**
	 * 是否是第一页
	 *
	 * @return
	 */
	boolean isFirst();

	/**
	 * 是否是最后一页
	 *
	 * @return
	 */
	boolean isLast();

	/**
	 * 是否有分页内容
	 *
	 * @return
	 */
	boolean hasContent();

	/**
	 * 获取分页内容
	 *
	 * @return
	 */
	List<T> getContent();
}
