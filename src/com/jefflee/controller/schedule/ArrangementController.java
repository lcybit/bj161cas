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

import com.jefflee.dto.schedule.ArrangementDto;
import com.jefflee.service.schedule.ArrangementService;

@RestController
@RequestMapping(value = "/arrangement")
public class ArrangementController {

	@Resource(name = "arrangementService")
	ArrangementService arrangementService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody ArrangementDto arrangementDto) {
		Map<String, String> result = new HashMap<String, String>();
		String arrangementId = arrangementService.create(arrangementDto);
		if (arrangementId != null) {
			result.put("arrangementId", arrangementId);
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<ArrangementDto> listAll() {
		return arrangementService.listAll();
	}

	@RequestMapping(value = "/find/{arrangementId}", method = RequestMethod.GET)
	public ArrangementDto findById(@PathVariable("arrangementId") Integer arrangementId) {
		return arrangementService.findById(arrangementId);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody ArrangementDto arrangementDto) {
		Map<String, String> result = new HashMap<String, String>();
		String arrangementId = arrangementService.modify(arrangementDto);
		if (arrangementId != null) {
			result.put("arrangementId", arrangementId);
		}
		return result;
	}

	@RequestMapping(value = "/delete/{arrangementId}", method = RequestMethod.DELETE)
	public Map<String, String> delete(@PathVariable("arrangementId") String arrangementId) {
		Map<String, String> result = new HashMap<String, String>();
		arrangementId = arrangementService.delete(arrangementId);
		if (arrangementId != null) {
			result.put("arrangementId", arrangementId);
		}
		return result;
	}

}
