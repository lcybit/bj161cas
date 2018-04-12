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
import com.jefflee.po.information.TclassPo;
import com.jefflee.po.schedule.GroupPo;
import com.jefflee.service.information.TclassService;
import com.jefflee.service.schedule.GroupService;

@RestController
@RequestMapping(value = "/tclass")
public class TclassController {

	@Resource(name = "tclassService")
	TclassService tclassService;
	
	@Resource(name = "groupService")
	GroupService groupService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody TclassPo tclassPo) {
		Map<String, String> result = new HashMap<String, String>();
		Integer tclassId = tclassService.insert(tclassPo);
		if (tclassId != null) {
			result.put("tclassId", tclassId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<TclassPo> listAll() {
		return tclassService.selectAll();
	}

	@RequestMapping(value = "/find/{tclassId}", method = RequestMethod.GET)
	public TclassPo findById(@PathVariable("tclassId") Integer tclassId) {
		return tclassService.selectById(tclassId);
	}
    
	//TODO delete
	@RequestMapping(value = "/check/{groupId}", method = RequestMethod.GET)
	public List<Tclass> checkById(@PathVariable("groupId") Integer groupId) {
		//根据groupid获取相应的year ，再根据year从tclass表中获取该年级的多个班级
		GroupPo groupPo = groupService.selectById(groupId);
		Integer year = groupPo.getYear();
		
		return tclassService.checkByYear(year);
	}
	
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody TclassPo tclassPo) {
		Map<String, String> result = new HashMap<String, String>();
		Integer tclassId = tclassService.updateById(tclassPo);
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
