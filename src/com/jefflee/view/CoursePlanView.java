package com.jefflee.view;

import java.util.Map;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.schedule.Plan;
import com.jefflee.po.information.CoursePo;

public class CoursePlanView {
	private CoursePo course;
	private Integer periodNum;	
	private Map<String, Plan> paneMap;
	
	
	public CoursePo getCourse() {
		return course;
	}
	public void setCourse(CoursePo course) {
		this.course = course;
	}
	public Integer getPeriodNum() {
		return periodNum;
	}
	public void setPeriodNum(Integer periodNum) {
		this.periodNum = periodNum;
	}
	public Map<String, Plan> getPaneMap() {
		return paneMap;
	}
	public void setPaneMap(Map<String, Plan> paneMap) {
		this.paneMap = paneMap;
	}
	
	

}
