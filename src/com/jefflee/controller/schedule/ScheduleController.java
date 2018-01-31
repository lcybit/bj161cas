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

import com.jefflee.dto.schedule.ScheduleDto;
import com.jefflee.service.schedule.ScheduleService;
import com.jefflee.view.ScheduleView;

@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController {

	@Resource(name = "scheduleService")
	ScheduleService scheduleService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody ScheduleDto scheduleDto) {
		Map<String, String> result = new HashMap<String, String>();
		Integer scheduleId = scheduleService.create(scheduleDto);
		if (scheduleId != null) {
			result.put("scheduleId", scheduleId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<ScheduleDto> listAll() {
		return scheduleService.listAll();
	}

	@RequestMapping(value = "/find/{scheduleId}", method = RequestMethod.GET)
	public ScheduleDto findById(@PathVariable("scheduleId") Integer scheduleId) {
		return scheduleService.findById(scheduleId);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody ScheduleDto scheduleDto) {
		Map<String, String> result = new HashMap<String, String>();
		Integer scheduleId = scheduleService.modify(scheduleDto);
		if (scheduleId != null) {
			result.put("scheduleId", scheduleId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/delete/{scheduleId}", method = RequestMethod.DELETE)
	public Map<String, String> delete(@PathVariable("scheduleId") Integer scheduleId) {
		Map<String, String> result = new HashMap<String, String>();
		scheduleId = scheduleService.delete(scheduleId);
		if (scheduleId != null) {
			result.put("scheduleId", scheduleId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/schedule/{scheduleId}", method = RequestMethod.GET)
	public ScheduleView schedule(@PathVariable("scheduleId") Integer scheduleId) {
		return scheduleService.schedule(scheduleId);
	}

}
