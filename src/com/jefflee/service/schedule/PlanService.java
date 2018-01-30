package com.jefflee.service.schedule;

import java.util.List;

import com.jefflee.dto.schedule.PlanDto;

public interface PlanService {

	public Integer create(PlanDto planDto);

	public List<PlanDto> listAll();

	public PlanDto findById(Integer planId);

	public Integer modify(PlanDto planDto);

	public Integer delete(Integer planId);

}
