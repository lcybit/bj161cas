package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.schedule.PlanDto;
import com.jefflee.dto.schedule.RelationDto;
import com.jefflee.entity.schedule.Plan;
import com.jefflee.mapper.schedule.PlanMapper;
import com.jefflee.service.schedule.PlanService;
import com.jefflee.service.schedule.RelationService;
import com.jefflee.util.BeanUtil;

@Service("planService")
public class PlanServiceImpl implements PlanService {

	@Resource(name = "planMapper")
	private PlanMapper planMapper;

	@Resource(name = "relationService")
	private RelationService relationService;

	@Override
	public Integer create(PlanDto planDto) {
		Plan plan = new Plan();
		BeanUtil.copyProperties(planDto, plan);
		if (planMapper.insert(plan) == 1) {
			return plan.getPlanId();
		} else {
			return null;
		}
	}

	@Override
	public List<PlanDto> listAll() {
		List<Plan> planList = planMapper.selectAll();
		List<PlanDto> planDtoList = new ArrayList<PlanDto>();
		for (Plan plan : planList) {
			PlanDto planDto = new PlanDto();
			BeanUtil.copyPropertiesSelective(plan, planDto);
			planDtoList.add(planDto);
		}
		return planDtoList;
	}

	@Override
	public PlanDto findById(Integer planId) {
		PlanDto planDto = new PlanDto();
		Plan plan = planMapper.selectByPrimaryKey(planId);
		BeanUtil.copyProperties(plan, planDto);
		return planDto;
	}

	@Override
	public Integer modify(PlanDto planDto) {
		Plan plan = new Plan();
		BeanUtil.copyProperties(planDto, plan);
		if (planMapper.updateByPrimaryKey(plan) == 1) {
			return plan.getPlanId();
		} else {
			return null;
		}
	}

	@Override
	public Integer delete(Integer planId) {
		if (planMapper.deleteByPrimaryKey(planId) == 1) {
			return planId;
		} else {
			return null;
		}
	}

	@Override
	public List<PlanDto> findByScheduleId(Integer scheduleId) {
		List<PlanDto> planDtoList = new ArrayList<PlanDto>();
		List<RelationDto> relationDtolList = relationService.findByScheduleId(scheduleId);
		for (RelationDto relationDto : relationDtolList) {
			PlanDto planDto = findByRelationId(relationDto.getRelationId());
			planDtoList.add(planDto);
		}
		return planDtoList;
	}

	@Override
	public PlanDto findByRelationId(Integer relationId) {
		Plan queryPlan = new Plan();
		queryPlan.setRelationId(relationId);
		Plan plan = planMapper.selectOne(queryPlan);
		PlanDto planDto = new PlanDto();
		BeanUtil.copyProperties(plan, planDto);
		return planDto;
	}
}
