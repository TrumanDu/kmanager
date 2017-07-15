package com.chickling.kmonitor.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.chickling.kmonitor.model.OffsetPoints;

/**
 * @author Hulva Luva.H
 *
 */
public class CommonUtils {
	public static void sortByTimestampThenPartition(List<OffsetPoints> offsetPointsList) {
		Collections.sort(offsetPointsList, new Comparator<OffsetPoints>() {

			@Override
			public int compare(OffsetPoints o1, OffsetPoints o2) {
				int flag = o1.getTimestamp().compareTo(o2.getTimestamp());
				if (flag == 0) {
					return o1.getPartition().compareTo(o2.getPartition());
				} else {
					return flag;
				}
			}
		});
	}
}
