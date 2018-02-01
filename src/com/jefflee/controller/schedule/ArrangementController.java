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

@RestController
@RequestMapping(value = "/arrangement")
public class ArrangementController {

	@Resource(name = "arrangementService")
	ArrangementService arrangementService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody ArrangementPo arrangementPo) {
		Map<String, String> result = new HashMap<String, String>();
		String arrangementId = arrangementService.insert(arrangementPo);
		if (arrangementId != null) {
			result.put("arrangementId", arrangementId);
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
		String arrangementId = arrangementService.updateById(arrangementPo);
		if (arrangementId != null) {
			result.put("arrangementId", arrangementId);
		}
		return result;
	}

	@RequestMapping(value = "/delete/{arrangementId}", method = RequestMethod.DELETE)
	public Map<String, String> delete(@PathVariable("arrangementId") String arrangementId) {
		Map<String, String> result = new HashMap<String, String>();
		arrangementId = arrangementService.deleteById(arrangementId);
		if (arrangementId != null) {
			result.put("arrangementId", arrangementId);
		}
		return result;
	}

}
