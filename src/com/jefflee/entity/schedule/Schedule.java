package com.jefflee.entity.schedule;

import com.jefflee.po.schedule.SchedulePo;

public class Schedule {
	public Integer scheduleId;
	public Group group;
	public Integer forenoon;
	public Integer afternoon;
	public Integer evening;
	public Integer days;
	public Integer startWeek;

	public Schedule() {
		this.group = new Group();
	}

	public Schedule(Integer scheduleId) {
		this.scheduleId = scheduleId;
		this.group = new Group();
	}

	public Schedule(SchedulePo schedulePo) {
		scheduleId = schedulePo.getScheduleId();
		group = new Group(schedulePo.getGroupId());
		forenoon = schedulePo.getForenoon();
		afternoon = schedulePo.getAfternoon();
		evening = schedulePo.getEvening();
		days = schedulePo.getDays();
		startWeek = schedulePo.getStartWeek();
	}

	public SchedulePo toPo() {
		SchedulePo schedulePo = new SchedulePo();
		schedulePo.setScheduleId(scheduleId);
		schedulePo.setGroupId(group.groupId);
		schedulePo.setForenoon(forenoon);
		schedulePo.setAfternoon(afternoon);
		schedulePo.setEvening(evening);
		schedulePo.setDays(days);
		schedulePo.setStartWeek(startWeek);
		return schedulePo;
	}
}
