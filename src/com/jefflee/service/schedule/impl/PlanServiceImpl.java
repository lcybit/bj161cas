package com.jefflee.service.schedule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.mapper.schedule.PlanMapper;
import com.jefflee.po.schedule.PlanPo;
import com.jefflee.service.schedule.ArrangementService;
import com.jefflee.service.schedule.PlanService;

@Service("planService")
public class PlanServiceImpl implements PlanService {

	@Resource(name = "planMapper")
	private PlanMapper planMapper;

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
		PlanPo selectPlanPo = new PlanPo();
		selectPlanPo.setScheduleId(scheduleId);
		return planMapper.select(selectPlanPo);
	}

}
