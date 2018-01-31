package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.schedule.PlanDto;
import com.jefflee.entity.schedule.Plan;
import com.jefflee.entity.schedule.Relation;
import com.jefflee.mapper.schedule.PlanMapper;
import com.jefflee.po.schedule.PlanPo;
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
		PlanPo planPo = new PlanPo();
		BeanUtil.copyProperties(planDto, planPo);
		if (planMapper.insert(planPo) == 1) {
			return planPo.getPlanId();
		} else {
			return null;
		}
	}

	@Override
	public List<PlanDto> listAll() {
		List<PlanPo> planList = planMapper.selectAll();
		List<PlanDto> planDtoList = new ArrayList<PlanDto>();
		for (PlanPo planPo : planList) {
			PlanDto planDto = new PlanDto();
			BeanUtil.copyPropertiesSelective(planPo, planDto);
			planDtoList.add(planDto);
		}
		return planDtoList;
	}

	@Override
	public PlanDto findById(Integer planId) {
		PlanDto planDto = new PlanDto();
		PlanPo planPo = planMapper.selectByPrimaryKey(planId);
		BeanUtil.copyProperties(planPo, planDto);
		return planDto;
	}

	@Override
	public Integer modify(PlanDto planDto) {
		PlanPo planPo = new PlanPo();
		BeanUtil.copyProperties(planDto, planPo);
		if (planMapper.updateByPrimaryKey(planPo) == 1) {
			return planPo.getPlanId();
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
	public List<Plan> findByScheduleId(Integer scheduleId) {
		List<Plan> planList = new ArrayList<Plan>();
		List<Relation> relationList = relationService.findByScheduleId(scheduleId);
		for (Relation relation : relationList) {
			Plan plan = findByRelationId(relation.relationId);
			planList.add(plan);
		}
		return planList;
	}

	@Override
	public Plan findByRelationId(Integer relationId) {
		PlanPo queryPlanPo = new PlanPo();
		queryPlanPo.setRelationId(relationId);
		PlanPo planPo = planMapper.selectOne(queryPlanPo);
		Plan plan = new Plan();
		BeanUtil.copyProperties(planPo, plan);
		return plan;
	}
}
