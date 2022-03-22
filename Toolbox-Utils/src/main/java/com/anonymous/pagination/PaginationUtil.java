package com.anonymous.pagination;

import com.sun.corba.se.impl.orbutil.ObjectUtility;
import com.sun.istack.internal.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *     逻辑层分页工具类
 *
 * @author HGK
 * @version 1.0
 * @date 2021/12/8 18:38
 */
public final class PaginationUtil {

	/**
	 * 【分页】根据分页数据获取总页数
	 *
	 * @param pageSize 页长
	 * @param totalCount 总记录数
	 * @return totalPage 总页数
	 */
	public static Integer getTotalPage(int pageSize, int totalCount) {
		return (totalCount / pageSize + ((totalCount % pageSize) > 0 ? 1 : 0));
	}

	/**
	 * 分页返回结果的map中key的枚举
	 * @see PaginationUtil#paginationSorting(java.lang.Integer, java.lang.Integer, java.util.List, java.util.Comparator)
	 */
	@AllArgsConstructor
	@Getter
	@SuppressWarnings({"all"})
	public enum PaginationParameterEnum {
		LIST("list", "当前页记录列表"),
		PAGE_NO("pageNo", "页码"),
		PAGE_SIZE("pageSize", "页长"),
		TOTAL_COUNT("totalCount", "总记录数"),
		TOTAL_PAGE("totalPage", "总页数");
		private final String key;
		@Getter(AccessLevel.NONE)
		private final String message;
	}

	/**
	 * 带排序的逻辑分页
	 *
	 * @param pageNo 页码
	 * @param pageSize 页长
	 * @param list 待分页记录列表
	 * @param comparator 排序
	 * @param <T>
	 * @return
	 * 		例如：
	 * 			{totalCount = 100, pageNo = 1, pageSize = 10, totalPage = 10, list = [{}, {}... ]}
	 */
	@Nullable
	public static <T> Map<String, Object> paginationSorting(int pageNo, int pageSize, @NonNull List<T> list, @Nullable Comparator<? super T> comparator) {

		if (list.isEmpty()) {
			return null;
		}

		// 排序
		if (null != comparator) {
			list.sort(comparator);
		}

		Map<String, Object> returnMap = new HashMap<>(8);
		// 记录总数
		returnMap.put(PaginationParameterEnum.TOTAL_COUNT.getKey(), list.size());
		// 页码
		returnMap.put(PaginationParameterEnum.PAGE_NO.getKey(), pageNo);
		// 页长
		returnMap.put(PaginationParameterEnum.PAGE_SIZE.getKey(), pageSize);
		// 总页数
		returnMap.put(PaginationParameterEnum.TOTAL_PAGE.getKey(), PaginationUtil.getTotalPage(pageSize, list.size()));

		// 起始索引
		int startIndex = (pageNo - 1) * pageSize;
		// 总记录数
		int total = list.size();

		// 记录总数 小于等于 起始索引，则代表此页已经没有数据
		if (total <= startIndex) {
			return returnMap;
		}

		// 结束索引
		int endIndex = pageNo * pageSize - 1;

		List<T> recordList;
		if (total > endIndex) {
			recordList = list.stream().skip(startIndex).limit(endIndex + 1).collect(Collectors.toList());
		} else {
			recordList = list.stream().skip(startIndex).limit(total - startIndex).collect(Collectors.toList());
		}
		// 本页记录列表
		returnMap.put(PaginationParameterEnum.LIST.getKey(), recordList);
		return returnMap;
	}

	private PaginationUtil() {}

}
