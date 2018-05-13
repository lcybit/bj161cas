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

import com.jefflee.entity.schedule.Arrangement;
import com.jefflee.service.schedule.ArrangementService;
import com.jefflee.view.ScheduleView;

@RestController
@RequestMapping(value = "/arrangement")
public class ArrangementController {

	@Resource(name = "arrangementService")
	ArrangementService arrangementService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody Arrangement arrangement) {
		Map<String, String> result = new HashMap<String, String>();
		Integer arrangementId = arrangementService.insert(arrangement);
		if (arrangementId != null) {
			result.put("arrangementId", arrangementId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Arrangement> listAll() {
		return arrangementService.selectList();
	}

	@RequestMapping(value = "/find/{arrangementId}", method = RequestMethod.GET)
	public Arrangement findById(@PathVariable("arrangementId") Integer arrangementId) {
		return arrangementService.selectById(arrangementId);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody Arrangement arrangement) {
		Map<String, String> result = new HashMap<String, String>();
		Integer arrangementId = arrangementService.updateById(arrangement);
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

	@RequestMapping(value = "/deleteAll/{scheduleId}", method = RequestMethod.DELETE)
	public Map<String, String> deleteAll(@PathVariable("scheduleId") Integer scheduleId) {
		Map<String, String> result = new HashMap<String, String>();
		arrangementService.deleteByScheduleId(scheduleId);
		result.put("done", "true");
		return result;
	}

	@RequestMapping(value = "/doAdjust/{adjustmentId}", method = RequestMethod.POST)
	public Map<String, String> doAdjust(@PathVariable Integer adjustmentId) {
		Map<String, String> result = new HashMap<String, String>();
		arrangementService.doAdjust(adjustmentId);
		result.put("done", "true");
		return result;
	}

	@RequestMapping(value = "/undoAdjust/{adjustmentId}", method = RequestMethod.POST)
	public Map<String, String> undoAdjust(@PathVariable Integer adjustmentId) {
		Map<String, String> result = new HashMap<String, String>();
		arrangementService.undoAdjust(adjustmentId);
		result.put("done", "true");
		return result;
	}

	@RequestMapping(value = "/saveAdjustment/{scheduleId}", method = RequestMethod.POST)
	public void saveAdjustment(@PathVariable Integer scheduleId) {
		arrangementService.saveAdjustment(scheduleId);
		return;
	}

	@RequestMapping(value = "/resetArrangement/{scheduleId}", method = RequestMethod.POST)
	public void resetArrangement(@PathVariable Integer scheduleId) {
		arrangementService.resetList(scheduleId);
		return;
	}

	@RequestMapping(value = "/getBackground/{scheduleId}", method = RequestMethod.GET)
	public Map<String, Map<String, Integer>> getBackgroundMap(@PathVariable Integer scheduleId) {
		return arrangementService.getBackgroundMap(scheduleId);
	}

	@RequestMapping(value = "/copy/{srcScheduleId}/{destScheduleId}", method = RequestMethod.POST)
	public Map<String, String> copy(@PathVariable("srcScheduleId") Integer srcScheduleId,
			@PathVariable("destScheduleId") Integer destScheduleId) {
		Map<String, String> result = new HashMap<String, String>();
		arrangementService.copyListByScheduleId(srcScheduleId, destScheduleId);
		result.put("done", "true");
		return result;
	}

	@RequestMapping(value = "/generate/{scheduleId}", method = RequestMethod.GET)
	public void generate(@PathVariable("scheduleId") Integer scheduleId) {
		arrangementService.gnrEmptyArrangementList(scheduleId);
	}

	@RequestMapping(value = "/display/{scheduleId}", method = RequestMethod.GET)
	public ScheduleView display(@PathVariable("scheduleId") Integer scheduleId) throws Exception {
		return arrangementService.gnrScheduleView(scheduleId);
	}

	@RequestMapping(value = "/arrange/{scheduleId}", method = RequestMethod.GET)
	public void arrange(@PathVariable("scheduleId") Integer scheduleId) {
		arrangementService.gnrArrangementList(scheduleId);
	}

	@RequestMapping(value = "/export/{scheduleId}", method = RequestMethod.GET)
	public void export(HttpServletResponse response, @PathVariable("scheduleId") Integer scheduleId) throws Exception {
		arrangementService.exportExcelView(response, scheduleId);
	}

	@RequestMapping(value = "/initial/{scheduleId}", method = RequestMethod.GET)
	public void initial(@PathVariable("scheduleId") Integer scheduleId) {
		arrangementService.initial(scheduleId);
	}
}
