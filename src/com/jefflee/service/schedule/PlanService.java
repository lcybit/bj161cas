package com.jefflee.service.schedule;

import java.util.List;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.po.schedule.PlanPo;
import com.jefflee.view.CourseView;

public interface PlanService {

	public Integer insert(PlanPo planPo);

	public List<PlanPo> selectAll();

	public PlanPo selectById(Integer planId);

	public Integer updateById(PlanPo planPo);

	public Integer deleteById(Integer planId);

	public List<PlanPo> selectByScheduleId(Integer scheduleId);

	public PlanPo selectByRelationId(Integer relationId);

	public CourseView gnrCourseView(Schedule schedule, Course course, Tclass tclass);

}
