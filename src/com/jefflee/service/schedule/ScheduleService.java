package com.jefflee.service.schedule;

import java.util.List;

import com.jefflee.dto.schedule.ScheduleDto;

public interface ScheduleService {

	public Integer create(ScheduleDto scheduleDto);

	public List<ScheduleDto> listAll();

	public ScheduleDto findById(Integer scheduleId);

	public Integer modify(ScheduleDto scheduleDto);

	public Integer delete(Integer scheduleId);

}
