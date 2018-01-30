package com.jefflee.service.schedule;

import java.util.List;

import com.jefflee.dto.schedule.GroupDto;

public interface GroupService {

	public Integer create(GroupDto groupDto);

	public List<GroupDto> listAll();

	public GroupDto findById(Integer groupId);

	public Integer modify(GroupDto groupDto);

	public Integer delete(Integer groupId);

}
