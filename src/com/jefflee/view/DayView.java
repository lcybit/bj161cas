package com.jefflee.view;

import java.util.List;

public class DayView {
	private String dayName;
	private List<PeriodView> periodViewList;

	public List<PeriodView> getPeriodViewList() {
		return periodViewList;
	}

	public void setPeriodViewList(List<PeriodView> periodViewList) {
		this.periodViewList = periodViewList;
	}

	public String getDayName() {
		return dayName;
	}

	public void setDayName(String dayName) {
		this.dayName = dayName;
	}

}
