package com.jefflee.service.schedule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.mapper.schedule.GroupMapper;
import com.jefflee.po.schedule.GroupPo;
import com.jefflee.service.schedule.GroupService;

@Service("groupService")
public class GroupServiceImpl implements GroupService {

	@Resource(name = "groupMapper")
	private GroupMapper groupMapper;

	@Override
	public Integer insert(GroupPo groupPo) {
		if (groupMapper.insert(groupPo) == 1) {
			return groupPo.getGroupId();
		} else {
			return null;
		}
	}

	@Override
	public List<GroupPo> selectAll() {
		return groupMapper.selectAll();
	}

	@Override
	public GroupPo selectById(Integer groupId) {
		return groupMapper.selectByPrimaryKey(groupId);
	}

	@Override
	public Integer updateById(GroupPo groupPo) {
		if (groupMapper.updateByPrimaryKey(groupPo) == 1) {
			return groupPo.getGroupId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer groupId) {
		if (groupMapper.deleteByPrimaryKey(groupId) == 1) {
			return groupId;
		} else {
			return null;
		}
	}
}
