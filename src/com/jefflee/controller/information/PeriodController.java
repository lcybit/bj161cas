package com.jefflee.controller.information;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jefflee.entity.information.Period;
import com.jefflee.service.information.PeriodService;

@RestController
@RequestMapping(value = "/period")
public class PeriodController {

	@Resource(name = "periodService")
	PeriodService periodService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody Period period) {
		Map<String, String> result = new HashMap<String, String>();
		Integer periodId = periodService.insert(period);
		if (periodId != null) {
			result.put("preriodId", periodId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Period> listAll() {
		return periodService.selectList();
	}

	@RequestMapping(value = "/find/{periodId}", method = RequestMethod.GET)
	public Period findById(@PathVariable("periodId") Integer periodId) {
		return periodService.selectById(periodId);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody Period period) {
		Map<String, String> result = new HashMap<String, String>();
		Integer periodId = periodService.updateById(period);
		if (periodId != null) {
			result.put("periodId", periodId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/delete/{periodId}", method = RequestMethod.DELETE)
	public Map<String, String> delete(@PathVariable("periodId") Integer periodId) {
		Map<String, String> result = new HashMap<String, String>();
		periodId = periodService.deleteById(periodId);
		if (periodId != null) {
			result.put("periodId", periodId.toString());
		}
		return result;
	}
}
