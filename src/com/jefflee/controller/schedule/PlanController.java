package com.jefflee.controller.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jefflee.entity.schedule.Plan;
import com.jefflee.service.schedule.PlanService;
import com.jefflee.view.SchedulePlanView;

@RestController
@RequestMapping(value = "/plan")
public class PlanController {

	@Resource(name = "planService")
	PlanService planService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody Plan plan) {
		Map<String, String> result = new HashMap<String, String>();
		Integer planId = planService.insert(plan);
		if (planId != null) {
			result.put("planId", planId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Plan> listAll() {
		return planService.selectList();
	}

	@RequestMapping(value = "/find/{planId}", method = RequestMethod.GET)
	public Plan findById(@PathVariable("planId") Integer planId) {
		return planService.selectById(planId);
	}

	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public List<Plan> find(@RequestBody Plan plan) {
		return planService.selectList(plan);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody Plan plan) {
		Map<String, String> result = new HashMap<String, String>();
		Integer planId = planService.updateById(plan);
		if (planId != null) {
			result.put("planId", planId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/delete/{planId}", method = RequestMethod.DELETE)
	public Map<String, String> delete(@PathVariable("planId") Integer planId) {
		Map<String, String> result = new HashMap<String, String>();
		planId = planService.deleteById(planId);
		if (planId != null) {
			result.put("planId", planId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/deleteAll/{scheduleId}", method = RequestMethod.DELETE)
	public Map<String, String> deleteAll(@PathVariable("scheduleId") Integer scheduleId) {
		Map<String, String> result = new HashMap<String, String>();
		planService.deleteByScheduleId(scheduleId);
		result.put("done", "true");
		return result;
	}

	@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
	public Map<String, String> remove(@RequestBody Plan plan) {
		Map<String, String> result = new HashMap<String, String>();
		planService.delete(plan);
		result.put("success", "true");
		return result;
	}

	@RequestMapping(value = "/copy/{srcScheduleId}/{destScheduleId}", method = RequestMethod.POST)
	public Map<String, String> copy(@PathVariable("srcScheduleId") Integer srcScheduleId,
			@PathVariable("destScheduleId") Integer destScheduleId) {
		Map<String, String> result = new HashMap<String, String>();
		planService.copyListByScheduleId(srcScheduleId, destScheduleId);
		result.put("done", "true");
		return result;
	}

	@RequestMapping(value = "/display/{scheduleId}", method = RequestMethod.GET)
	public SchedulePlanView display(@PathVariable("scheduleId") Integer scheduleId) {
		return planService.gnrSchedulePlanView(scheduleId);
	}

	@RequestMapping(value = "/updatePeriodNum", method = RequestMethod.POST)
	public Map<String, String> updatePeriodNum(@RequestBody Plan plan) {
		Map<String, String> result = new HashMap<String, String>();
		planService.updatePeriodNum(plan);
		result.put("done", "true");
		return result;
	}
}
