package com.jefflee.view;

public class PeriodView {
	private String periodViewId;
	private Integer scheduleId;
	private Integer periodId;

	public Integer getPeriodId() {
		return periodId;
	}

	public void setPeriodId(Integer periodId) {
		this.periodId = periodId;
	}

	public String getPeriodViewId() {
		return periodViewId;
	}

	public void setPeriodViewId(String periodViewId) {
		this.periodViewId = periodViewId;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}
}
