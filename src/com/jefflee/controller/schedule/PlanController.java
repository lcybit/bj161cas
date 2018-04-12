package com.jefflee.controller.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jefflee.entity.schedule.Plan;
import com.jefflee.po.schedule.PlanPo;
import com.jefflee.service.schedule.PlanService;

@RestController
@RequestMapping(value = "/plan")
public class PlanController {

	@Resource(name = "planService")
	PlanService planService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody PlanPo planPo) {
		Map<String, String> result = new HashMap<String, String>();
		Integer planId = planService.insert(planPo);
		if (planId != null) {
			result.put("planId", planId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<PlanPo> listAll() {
		return planService.selectAll();
	}

	@RequestMapping(value = "/find/{planId}", method = RequestMethod.GET)
	public PlanPo findById(@PathVariable("planId") Integer planId) {
		return planService.selectById(planId);
	}

	@RequestMapping(value = "/check/{planId}", method = RequestMethod.GET)
	public Plan checkById(@PathVariable("planId") Integer planId) {
		return planService.findById(planId);
	}

	@RequestMapping(value = "/look/{courseId}/{scheduleId}", method = RequestMethod.GET)
	public List<PlanPo> lookById(@PathVariable("courseId") Integer courseId,
			@PathVariable("scheduleId") Integer scheduleId) {
		return planService.getPlanById(courseId, scheduleId);
	}

	@RequestMapping(value = "/rewrite", method = RequestMethod.POST)
	public Map<String, String> rewrite(@RequestParam("pidNum") Integer pidNum,
			@RequestParam("courseId") Integer courseId, @RequestParam("scheduleId") Integer scheduleId) {
		Integer num;
		Map<String, String> result = new HashMap<String, String>();
		List<PlanPo> planPoList = new ArrayList<PlanPo>();
		planPoList = planService.getPlanById(courseId, scheduleId);

		for (PlanPo planPo : planPoList) {
			planPo.setPeriodNum(pidNum);
			num = planService.updateById(planPo);
		}

		return result;
	}

	@RequestMapping(value = "/change", method = RequestMethod.POST)
	public Map<String, String> change(@RequestParam("planId") Integer planid,
			@RequestParam("teacherId") Integer teacherid) {
		Integer num;
		Map<String, String> result = new HashMap<String, String>();
		PlanPo planPo = new PlanPo();
		planPo = planService.selectById(planid);
		planPo.setTeacherId(teacherid);
		num = planService.updateById(planPo);
		return result;
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody PlanPo planPo) {
		Map<String, String> result = new HashMap<String, String>();
		Integer planId = planService.updateById(planPo);
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

}
