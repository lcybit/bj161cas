package com.jefflee.entity.schedule;

import com.jefflee.po.schedule.GroupPo;

public class Group {
	public Integer groupId;
	public Integer year;
	public Integer semester;
	public Integer level;
	public Integer grade;
	public String startDate;

	public Group() {
	}

	public Group(Integer groupId) {
		this.groupId = groupId;
	}

	public Group(GroupPo groupPo) {
		groupId = groupPo.getGroupId();
		year = groupPo.getYear();
		semester = groupPo.getSemester();
		level = groupPo.getLevel();
		grade = groupPo.getGrade();
		startDate = groupPo.getStartDate();
	}

	public GroupPo toPo() {
		GroupPo groupPo = new GroupPo();
		groupPo.setGroupId(groupId);
		groupPo.setYear(year);
		groupPo.setSemester(semester);
		groupPo.setLevel(level);
		groupPo.setGrade(grade);
		groupPo.setStartDate(startDate);
		return groupPo;
	}
}
