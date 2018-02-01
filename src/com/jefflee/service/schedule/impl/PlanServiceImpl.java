package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.mapper.schedule.PlanMapper;
import com.jefflee.po.schedule.ArrangementPo;
import com.jefflee.po.schedule.PlanPo;
import com.jefflee.po.schedule.RelationPo;
import com.jefflee.service.schedule.ArrangementService;
import com.jefflee.service.schedule.PlanService;
import com.jefflee.service.schedule.RelationService;
import com.jefflee.view.CourseView;

@Service("planService")
public class PlanServiceImpl implements PlanService {

	@Resource(name = "planMapper")
	private PlanMapper planMapper;

	@Resource(name = "relationService")
	private RelationService relationService;
	@Resource(name = "arrangementService")
	private ArrangementService arrangementService;

	@Override
	public Integer insert(PlanPo planPo) {
		if (planMapper.insert(planPo) == 1) {
			return planPo.getPlanId();
		} else {
			return null;
		}
	}

	@Override
	public List<PlanPo> selectAll() {
		return planMapper.selectAll();
	}

	@Override
	public PlanPo selectById(Integer planId) {
		return planMapper.selectByPrimaryKey(planId);
	}

	@Override
	public Integer updateById(PlanPo planPo) {
		if (planMapper.updateByPrimaryKey(planPo) == 1) {
			return planPo.getPlanId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer planId) {
		if (planMapper.deleteByPrimaryKey(planId) == 1) {
			return planId;
		} else {
			return null;
		}
	}

	// TODO
	@Override
	public List<PlanPo> selectByScheduleId(Integer scheduleId) {
		List<PlanPo> planPoList = new ArrayList<PlanPo>();
		List<RelationPo> relationPoList = relationService.selectByScheduleId(scheduleId);
		for (RelationPo relation : relationPoList) {
			planPoList.add(selectByRelationId(relation.getRelationId()));
		}
		return planPoList;
	}

	@Override
	public PlanPo selectByRelationId(Integer relationId) {
		PlanPo queryPlanPo = new PlanPo();
		queryPlanPo.setRelationId(relationId);
		return planMapper.selectOne(queryPlanPo);
	}

	@Override
	public CourseView gnrCourseView(Schedule schedule, Course course, Tclass tclass) {
		CourseView courseView = new CourseView();
		courseView.setCourse(course);

		RelationPo queryRelationPo = new RelationPo();
		queryRelationPo.setCourseId(course.courseId);
		queryRelationPo.setTclassId(tclass.tclassId);
		queryRelationPo.setScheduleId(schedule.scheduleId);
		RelationPo relationPo = relationService.selectByCombinedId(queryRelationPo).get(0);
		Integer periodNum = selectByRelationId(relationPo.getRelationId()).getPeriodNum();

		ArrangementPo queryArrangementPo = new ArrangementPo();
		queryArrangementPo.setArranged(1);
		queryArrangementPo.setRelationId(relationPo.getRelationId());
		Integer arrangedNum = arrangementService.selectCount(queryArrangementPo);

		courseView.setUnArrangedNum(periodNum - arrangedNum);

		return courseView;
	}
}
