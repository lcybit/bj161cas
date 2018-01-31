package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.schedule.ScheduleDto;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.mapper.schedule.ScheduleMapper;
import com.jefflee.po.schedule.SchedulePo;
import com.jefflee.service.schedule.PlanService;
import com.jefflee.service.schedule.ScheduleService;
import com.jefflee.util.BeanUtil;
import com.jefflee.view.ScheduleView;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

	@Resource(name = "scheduleMapper")
	private ScheduleMapper scheduleMapper;

	@Resource(name = "planService")
	private PlanService planService;

	@Override
	public Integer create(ScheduleDto scheduleDto) {
		Schedule schedule = new Schedule(scheduleDto);
		SchedulePo schedulePo = schedule.toPo();
		if (scheduleMapper.insert(schedulePo) == 1) {
			return schedulePo.getScheduleId();
		} else {
			return null;
		}
	}

	@Override
	public List<ScheduleDto> listAll() {
		List<SchedulePo> schedulePoList = scheduleMapper.selectAll();
		List<ScheduleDto> scheduleDtoList = new ArrayList<ScheduleDto>();
		for (SchedulePo schedulePo : schedulePoList) {
			scheduleDtoList.add(new Schedule(schedulePo).toDto());
		}
		return scheduleDtoList;
	}

	@Override
	public ScheduleDto findById(Integer scheduleId) {
		SchedulePo schedulePo = scheduleMapper.selectByPrimaryKey(scheduleId);
		return new Schedule(schedulePo).toDto();
	}

	@Override
	public Integer modify(ScheduleDto scheduleDto) {
		SchedulePo schedulePo = new Schedule(scheduleDto).toPo();
		if (scheduleMapper.updateByPrimaryKey(schedulePo) == 1) {
			return schedulePo.getScheduleId();
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
	public ScheduleView schedule(Integer scheduleId) {
		ScheduleView scheduleView = new ScheduleView();
		Schedule schedule = new Schedule(scheduleMapper.selectByPrimaryKey(scheduleId));

		return null;
	}
}
