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

import com.jefflee.dto.schedule.GroupDto;
import com.jefflee.service.schedule.GroupService;

@RestController
@RequestMapping(value = "/group")
public class GroupController {

	@Resource(name = "groupService")
	GroupService groupService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody GroupDto groupDto) {
		Map<String, String> result = new HashMap<String, String>();
		Integer groupId = groupService.create(groupDto);
		if (groupId != null) {
			result.put("groupId", groupId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<GroupDto> listAll() {
		return groupService.listAll();
	}

	@RequestMapping(value = "/find/{groupId}", method = RequestMethod.GET)
	public GroupDto findById(@PathVariable("groupId") Integer groupId) {
		return groupService.findById(groupId);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody GroupDto groupDto) {
		Map<String, String> result = new HashMap<String, String>();
		Integer groupId = groupService.modify(groupDto);
		if (groupId != null) {
			result.put("groupId", groupId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/delete/{groupId}", method = RequestMethod.DELETE)
	public Map<String, String> delete(@PathVariable("groupId") Integer groupId) {
		Map<String, String> result = new HashMap<String, String>();
		groupId = groupService.delete(groupId);
		if (groupId != null) {
			result.put("groupId", groupId.toString());
		}
		return result;
	}
}
