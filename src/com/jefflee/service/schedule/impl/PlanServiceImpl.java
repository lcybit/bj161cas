package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.schedule.PlanDto;
import com.jefflee.entity.schedule.Plan;
import com.jefflee.mapper.schedule.PlanMapper;
import com.jefflee.service.schedule.PlanService;
import com.jefflee.util.BeanUtil;

import tk.mybatis.mapper.entity.Example;

@Service("planService")
public class PlanServiceImpl implements PlanService {

	@Resource(name = "planMapper")
	private PlanMapper planMapper;

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
		Example example = new Example(Plan.class);
		example.setOrderByClause("plan_no ASC");
		List<Plan> planList = planMapper.selectByExample(example);
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
}
