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

import com.jefflee.po.schedule.ArrangementPo;
import com.jefflee.service.schedule.ArrangementService;
import com.jefflee.view.TclassPeriodView;

@RestController
@RequestMapping(value = "/arrangement")
public class ArrangementController {

	@Resource(name = "arrangementService")
	ArrangementService arrangementService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody ArrangementPo arrangementPo) {
		Map<String, String> result = new HashMap<String, String>();
		Integer arrangementId = arrangementService.insert(arrangementPo);
		if (arrangementId != null) {
			result.put("arrangementId", arrangementId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<ArrangementPo> listAll() {
		return arrangementService.selectAll();
	}

	@RequestMapping(value = "/find/{arrangementId}", method = RequestMethod.GET)
	public ArrangementPo findById(@PathVariable("arrangementId") Integer arrangementId) {
		return arrangementService.selectById(arrangementId);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody ArrangementPo arrangementPo) {
		Map<String, String> result = new HashMap<String, String>();
		Integer arrangementId = arrangementService.updateById(arrangementPo);
		if (arrangementId != null) {
			result.put("arrangementId", arrangementId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/delete/{arrangementId}", method = RequestMethod.DELETE)
	public Map<String, String> delete(@PathVariable("arrangementId") Integer arrangementId) {
		Map<String, String> result = new HashMap<String, String>();
		arrangementId = arrangementService.deleteById(arrangementId);
		if (arrangementId != null) {
			result.put("arrangementId", arrangementId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/arrange", method = RequestMethod.POST)
	public Map<String, String> arrange(@RequestBody TclassPeriodView tclassPeriodView) {
		Map<String, String> result = new HashMap<String, String>();
		arrangementService.updateArrangement(tclassPeriodView, 1, -1);
		result.put("done", "true");
		return result;
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public Map<String, String> cancel(@RequestBody TclassPeriodView tclassPeriodView) {
		Map<String, String> result = new HashMap<String, String>();
		arrangementService.updateArrangement(tclassPeriodView, 0, -1);
		result.put("done", "true");
		return result;
	}

	@RequestMapping(value = "/check/schedule/{scheduleId}/course/{courseId}", method = RequestMethod.GET)
	public List<Map<String, String>> checkConflict(@PathVariable("scheduleId") Integer scheduleId,
			@PathVariable("courseId") Integer courseId) {
		List<Map<String, String>> conflictList = arrangementService.gnrConflictMap(scheduleId, courseId);
		return conflictList;
	}

}
