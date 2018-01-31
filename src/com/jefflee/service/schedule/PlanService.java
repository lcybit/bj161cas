package com.jefflee.service.schedule;

import java.util.List;

import com.jefflee.dto.schedule.PlanDto;
import com.jefflee.entity.schedule.Plan;

public interface PlanService {

	public Integer create(PlanDto planDto);

	public List<PlanDto> listAll();

	public PlanDto findById(Integer planId);

	public Integer modify(PlanDto planDto);

	public Integer delete(Integer planId);

	public List<Plan> findByScheduleId(Integer scheduleId);

	public Plan findByRelationId(Integer relationId);

}
