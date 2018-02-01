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

import com.jefflee.po.schedule.RelationPo;
import com.jefflee.service.schedule.RelationService;

@RestController
@RequestMapping(value = "/relation")
public class RelationController {

	@Resource(name = "relationService")
	RelationService relationService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody RelationPo relationPo) {
		Map<String, String> result = new HashMap<String, String>();
		Integer relationId = relationService.insert(relationPo);
		if (relationId != null) {
			result.put("relationId", relationId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<RelationPo> listAll() {
		return relationService.selectAll();
	}

	@RequestMapping(value = "/find/{relationId}", method = RequestMethod.GET)
	public RelationPo findById(@PathVariable("relationId") Integer relationId) {
		return relationService.selectById(relationId);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody RelationPo relationPo) {
		Map<String, String> result = new HashMap<String, String>();
		Integer relationId = relationService.updateById(relationPo);
		if (relationId != null) {
			result.put("relationId", relationId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/delete/{relationId}", method = RequestMethod.DELETE)
	public Map<String, String> delete(@PathVariable("relationId") Integer relationId) {
		Map<String, String> result = new HashMap<String, String>();
		relationId = relationService.deleteById(relationId);
		if (relationId != null) {
			result.put("relationId", relationId.toString());
		}
		return result;
	}

}
