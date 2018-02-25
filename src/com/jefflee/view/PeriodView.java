package com.jefflee.view;

import com.jefflee.entity.schedule.Arrangement;

public class PeriodView {
	private String periodViewId;
	private Integer scheduleId;
	private Arrangement arrangement;

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

	public Arrangement getArrangement() {
		return arrangement;
	}

	public void setArrangement(Arrangement arrangement) {
		this.arrangement = arrangement;
	}

}
