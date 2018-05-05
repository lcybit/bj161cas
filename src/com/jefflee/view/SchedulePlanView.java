package com.jefflee.view;

import java.util.List;
import java.util.Map;

import com.jefflee.entity.information.Tclass;

public class SchedulePlanView {

	private List<Tclass> tclassList;
	private Map<String, CoursePlanView> coursePlanViewMap;

	public List<Tclass> getTclassList() {
		return tclassList;
	}

	public void setTclassList(List<Tclass> tclassList) {
		this.tclassList = tclassList;
	}

	public Map<String, CoursePlanView> getCoursePlanViewMap() {
		return coursePlanViewMap;
	}

	public void setCoursePlanViewMap(Map<String, CoursePlanView> coursePlanViewMap) {
		this.coursePlanViewMap = coursePlanViewMap;
	}

}
