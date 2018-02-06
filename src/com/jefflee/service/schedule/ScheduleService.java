package com.jefflee.service.schedule;

import java.util.List;

import com.jefflee.po.schedule.SchedulePo;
import com.jefflee.view.ScheduleView;

public interface ScheduleService {

	public Integer insert(SchedulePo schedulePo);

	public List<SchedulePo> selectAll();

	public SchedulePo selectById(Integer scheduleId);

	public Integer updateById(SchedulePo schedulePo);

	public Integer deleteById(Integer scheduleId);

	public ScheduleView gnrScheduleView(Integer scheduleId);

	public void gnrAllArrangementList(Integer scheduleId);

}
