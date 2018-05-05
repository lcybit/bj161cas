package com.jefflee.controller.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jefflee.entity.schedule.Schedule;
import com.jefflee.service.information.TclassService;
import com.jefflee.service.schedule.ScheduleService;
import com.jefflee.view.ScheduleView;

@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController {

	@Resource(name = "scheduleService")
	ScheduleService scheduleService;

	@Resource(name = "tclassService")
	TclassService tclassService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody Schedule schedule) {
		Map<String, String> result = new HashMap<String, String>();
		Integer scheduleId = scheduleService.insert(schedule);
		if (scheduleId != null) {
			result.put("scheduleId", scheduleId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list/{gradeId}", method = RequestMethod.GET)
	public List<Schedule> listByGradeId(@PathVariable("gradeId") Integer gradeId) {
		return scheduleService.selectListByGradeId(gradeId);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Schedule> listAll() {
		return scheduleService.selectList();
	}

	@RequestMapping(value = "/find/{scheduleId}", method = RequestMethod.GET)
	public Schedule findById(@PathVariable("scheduleId") Integer scheduleId) {
		return scheduleService.selectById(scheduleId);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody Schedule schedule) {
		Map<String, String> result = new HashMap<String, String>();
		Integer scheduleId = scheduleService.updateById(schedule);
		if (scheduleId != null) {
			result.put("scheduleId", scheduleId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/delete/{scheduleId}", method = RequestMethod.DELETE)
	public Map<String, String> delete(@PathVariable("scheduleId") Integer scheduleId) {
		Map<String, String> result = new HashMap<String, String>();
		scheduleId = scheduleService.deleteById(scheduleId);
		if (scheduleId != null) {
			result.put("scheduleId", scheduleId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/display/{scheduleId}", method = RequestMethod.GET)
	public ScheduleView display(@PathVariable("scheduleId") Integer scheduleId) {
		return scheduleService.gnrScheduleView(scheduleId);
	}

	@RequestMapping(value = "/generate/{scheduleId}", method = RequestMethod.GET)
	public void generate(@PathVariable("scheduleId") Integer scheduleId) {
		scheduleService.gnrEmptyArrangementList(scheduleId);
	}

	@RequestMapping(value = "/arrange/{scheduleId}", method = RequestMethod.GET)
	public void arrange(@PathVariable("scheduleId") Integer scheduleId) {
		scheduleService.gnrSchedule(scheduleId);
	}

	@RequestMapping(value = "/export/{scheduleId}", method = RequestMethod.GET)
	public void export(HttpServletResponse response, @PathVariable("scheduleId") Integer scheduleId) throws Exception {
		scheduleService.exportExcelView(response, scheduleId);
	}

	@RequestMapping(value = "/initial/{id}", method = RequestMethod.GET)
	public void initial(@PathVariable("id") Integer scheduleId) {
		scheduleService.initial(scheduleId);
	}

}
