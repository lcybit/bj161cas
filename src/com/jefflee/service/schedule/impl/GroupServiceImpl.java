package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.schedule.GroupDto;
import com.jefflee.entity.schedule.Group;
import com.jefflee.mapper.schedule.GroupMapper;
import com.jefflee.service.schedule.GroupService;
import com.jefflee.util.BeanUtil;

@Service("groupService")
public class GroupServiceImpl implements GroupService {

	@Resource(name = "groupMapper")
	private GroupMapper groupMapper;

	@Override
	public Integer create(GroupDto groupDto) {
		Group group = new Group();
		BeanUtil.copyProperties(groupDto, group);
		if (groupMapper.insert(group) == 1) {
			return group.getGroupId();
		} else {
			return null;
		}
	}

	@Override
	public List<GroupDto> listAll() {
		List<Group> groupList = groupMapper.selectAll();
		List<GroupDto> groupDtoList = new ArrayList<GroupDto>();
		for (Group group : groupList) {
			GroupDto groupDto = new GroupDto();
			BeanUtil.copyPropertiesSelective(group, groupDto);
			groupDtoList.add(groupDto);
		}
		return groupDtoList;
	}

	@Override
	public GroupDto findById(Integer groupId) {
		GroupDto groupDto = new GroupDto();
		Group group = groupMapper.selectByPrimaryKey(groupId);
		BeanUtil.copyProperties(group, groupDto);
		return groupDto;
	}

	@Override
	public Integer modify(GroupDto groupDto) {
		Group group = new Group();
		BeanUtil.copyProperties(groupDto, group);
		if (groupMapper.updateByPrimaryKey(group) == 1) {
			return group.getGroupId();
		} else {
			return null;
		}
	}

	@Override
	public Integer delete(Integer groupId) {
		if (groupMapper.deleteByPrimaryKey(groupId) == 1) {
			return groupId;
		} else {
			return null;
		}
	}
}
