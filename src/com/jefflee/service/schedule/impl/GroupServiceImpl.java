package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.schedule.GroupDto;
import com.jefflee.mapper.schedule.GroupMapper;
import com.jefflee.po.schedule.GroupPo;
import com.jefflee.service.schedule.GroupService;
import com.jefflee.util.BeanUtil;

@Service("groupService")
public class GroupServiceImpl implements GroupService {

	@Resource(name = "groupMapper")
	private GroupMapper groupMapper;

	@Override
	public Integer create(GroupDto groupDto) {
		GroupPo groupPo = new GroupPo();
		BeanUtil.copyProperties(groupDto, groupPo);
		if (groupMapper.insert(groupPo) == 1) {
			return groupPo.getGroupId();
		} else {
			return null;
		}
	}

	@Override
	public List<GroupDto> listAll() {
		List<GroupPo> groupList = groupMapper.selectAll();
		List<GroupDto> groupDtoList = new ArrayList<GroupDto>();
		for (GroupPo groupPo : groupList) {
			GroupDto groupDto = new GroupDto();
			BeanUtil.copyPropertiesSelective(groupPo, groupDto);
			groupDtoList.add(groupDto);
		}
		return groupDtoList;
	}

	@Override
	public GroupDto findById(Integer groupId) {
		GroupDto groupDto = new GroupDto();
		GroupPo groupPo = groupMapper.selectByPrimaryKey(groupId);
		BeanUtil.copyProperties(groupPo, groupDto);
		return groupDto;
	}

	@Override
	public Integer modify(GroupDto groupDto) {
		GroupPo groupPo = new GroupPo();
		BeanUtil.copyProperties(groupDto, groupPo);
		if (groupMapper.updateByPrimaryKey(groupPo) == 1) {
			return groupPo.getGroupId();
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
