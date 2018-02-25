package com.jefflee.view;

import com.jefflee.entity.schedule.Plan;

public class PlanView {
	private Plan plan;
	private Integer arrangedNum;

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Integer getArrangedNum() {
		return arrangedNum;
	}

	public void setArrangedNum(Integer arrangedNum) {
		this.arrangedNum = arrangedNum;
	}

}
