package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.schedule.PlanDto;
import com.jefflee.dto.schedule.ScheduleDto;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.mapper.schedule.ScheduleMapper;
import com.jefflee.service.schedule.PlanService;
import com.jefflee.service.schedule.ScheduleService;
import com.jefflee.util.BeanUtil;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

	@Resource(name = "scheduleMapper")
	private ScheduleMapper scheduleMapper;

	@Resource(name = "planService")
	private PlanService planService;

	@Override
	public Integer create(ScheduleDto scheduleDto) {
		Schedule schedule = new Schedule();
		BeanUtil.copyProperties(scheduleDto, schedule);
		if (scheduleMapper.insert(schedule) == 1) {
			return schedule.getScheduleId();
		} else {
			return null;
		}
	}

	@Override
	public List<ScheduleDto> listAll() {
		List<Schedule> scheduleList = scheduleMapper.selectAll();
		List<ScheduleDto> scheduleDtoList = new ArrayList<ScheduleDto>();
		for (Schedule schedule : scheduleList) {
			ScheduleDto scheduleDto = new ScheduleDto();
			BeanUtil.copyPropertiesSelective(schedule, scheduleDto);
			scheduleDtoList.add(scheduleDto);
		}
		return scheduleDtoList;
	}

	@Override
	public ScheduleDto findById(Integer scheduleId) {
		ScheduleDto scheduleDto = new ScheduleDto();
		Schedule schedule = scheduleMapper.selectByPrimaryKey(scheduleId);
		BeanUtil.copyProperties(schedule, scheduleDto);
		return scheduleDto;
	}

	@Override
	public Integer modify(ScheduleDto scheduleDto) {
		Schedule schedule = new Schedule();
		BeanUtil.copyProperties(scheduleDto, schedule);
		if (scheduleMapper.updateByPrimaryKey(schedule) == 1) {
			return schedule.getScheduleId();
		} else {
			return null;
		}
	}

	@Override
	public Integer delete(Integer scheduleId) {
		if (scheduleMapper.deleteByPrimaryKey(scheduleId) == 1) {
			return scheduleId;
		} else {
			return null;
		}
	}

	@Override
	public Integer schedule(Integer scheduleId) {
		ScheduleDto scheduleDto = findById(scheduleId);
		List<PlanDto> planDtoList = planService.findByScheduleId(scheduleId);
		return null;
	}
}
