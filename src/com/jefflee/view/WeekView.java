package com.jefflee.view;

import java.util.List;

public class WeekView {
	private String type;
	private Integer typeId;
	private String typeName;
	private List<DayView> dayViewList;
	private List<PlanView> planViewList;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

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

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
