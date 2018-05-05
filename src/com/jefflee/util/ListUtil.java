
package com.jefflee.util;

import java.util.List;
import java.util.Random;

public class ListUtil {

	public static <T> T randomlyChoose(List<T> tList) {
		Random random = new Random();
		T t = tList.get(random.nextInt(tList.size()));
		return t;
	}

}
