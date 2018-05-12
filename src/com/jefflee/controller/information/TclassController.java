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

import com.jefflee.entity.information.Tclass;
import com.jefflee.service.information.TclassService;
import com.jefflee.service.schedule.GradeService;

@RestController
@RequestMapping(value = "/tclass")
public class TclassController {

	@Resource(name = "tclassService")
	TclassService tclassService;

	@Resource(name = "gradeService")
	GradeService gradeService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody Tclass tclass) {
		Map<String, String> result = new HashMap<String, String>();
		Integer tclassId = tclassService.insert(tclass);
		if (tclassId != null) {
			result.put("tclassId", tclassId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Tclass> listAll() {
		return tclassService.selectList();
	}

	@RequestMapping(value = "/find/{tclassId}", method = RequestMethod.GET)
	public Tclass findById(@PathVariable("tclassId") Integer tclassId) {
		return tclassService.selectById(tclassId);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody Tclass tclass) {
		Map<String, String> result = new HashMap<String, String>();
		Integer tclassId = tclassService.updateById(tclass);
		if (tclassId != null) {
			result.put("tclassId", tclassId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/delete/{tclassId}", method = RequestMethod.DELETE)
	public Map<String, String> delete(@PathVariable("tclassId") Integer tclassId) {
		Map<String, String> result = new HashMap<String, String>();
		tclassId = tclassService.deleteById(tclassId);
		if (tclassId != null) {
			result.put("tclassId", tclassId.toString());
		}
		return result;
	}
}
