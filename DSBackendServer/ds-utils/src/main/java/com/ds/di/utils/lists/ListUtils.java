/**
 * 
 */
package com.ds.di.utils.lists;

import java.util.List;

/**
 * @author Altin Cipi
 *
 */
public class ListUtils
{
	public static boolean isEmptyList(List<?> lst)
	{
		return lst == null || lst.size() == 0;
	}

	public static int size(List<?> lst)
	{
		return ListUtils.notEmptyList(lst) ? lst.size() : 0;
	}

	public static boolean notEmptyList(List<?> lst)
	{
		return lst != null && lst.size() > 0;
	}

	public static <T> T getFirstElement(List<T> lst)
	{
		if (isEmptyList(lst))
			return null;
		else
			return (T) lst.get(0);
	}
}
