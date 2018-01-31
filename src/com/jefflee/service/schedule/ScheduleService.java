package com.jefflee.service.schedule;

import java.util.List;

import com.jefflee.dto.schedule.ScheduleDto;
import com.jefflee.view.ScheduleView;

public interface ScheduleService {

	public Integer create(ScheduleDto scheduleDto);

	public List<ScheduleDto> listAll();

	public ScheduleDto findById(Integer scheduleId);

	public Integer modify(ScheduleDto scheduleDto);

	public Integer delete(Integer scheduleId);

	public ScheduleView schedule(Integer scheduleId);

}
