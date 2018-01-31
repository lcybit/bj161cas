package com.jefflee.entity.schedule;

import com.jefflee.dto.schedule.ScheduleDto;
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
	}

	public Schedule(ScheduleDto scheduleDto) {
		fromDto(scheduleDto);
	}

	public Schedule(SchedulePo schedulePo) {
		fromPo(schedulePo);
	}

	public Schedule fromDto(ScheduleDto scheduleDto) {
		scheduleId = scheduleDto.getScheduleId();
		group = new Group();
		group.groupId = scheduleDto.getGroupId();
		forenoon = scheduleDto.getForenoon();
		afternoon = scheduleDto.getAfternoon();
		evening = scheduleDto.getEvening();
		days = scheduleDto.getDays();
		startWeek = scheduleDto.getStartWeek();
		return this;
	}

	public Schedule fromPo(SchedulePo schedulePo) {
		scheduleId = schedulePo.getScheduleId();
		group = new Group();
		group.groupId = schedulePo.getGroupId();
		forenoon = schedulePo.getForenoon();
		afternoon = schedulePo.getAfternoon();
		evening = schedulePo.getEvening();
		days = schedulePo.getDays();
		startWeek = schedulePo.getStartWeek();
		return this;
	}

	public ScheduleDto toDto() {
		ScheduleDto scheduleDto = new ScheduleDto();
		scheduleDto.setScheduleId(scheduleId);
		scheduleDto.setGroupId(group.groupId);
		scheduleDto.setForenoon(forenoon);
		scheduleDto.setAfternoon(afternoon);
		scheduleDto.setEvening(evening);
		scheduleDto.setDays(days);
		scheduleDto.setStartWeek(startWeek);
		return scheduleDto;
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
