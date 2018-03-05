package com.jefflee.view;

import java.util.List;

public class WeekView {
	private TitleView titleView;
	private List<DayView> dayViewList;
	private List<PlanView> planViewList;

	public List<DayView> getDayViewList() {
		return dayViewList;
	}

	public void setDayViewList(List<DayView> dayViewList) {
		this.dayViewList = dayViewList;
	}

	public List<PlanView> getPlanViewList() {
		return planViewList;
	}

	public void setPlanViewList(List<PlanView> planViewList) {
		this.planViewList = planViewList;
	}

	public TitleView getTitleView() {
		return titleView;
	}

	public void setTitleView(TitleView titleView) {
		this.titleView = titleView;
	}

}
