package com.jefflee.service.schedule;

import java.util.List;

import com.jefflee.dto.schedule.RelationDto;

public interface RelationService {

	public Integer create(RelationDto relationDto);

	public List<RelationDto> listAll();

	public RelationDto findById(Integer relationId);

	public Integer modify(RelationDto relationDto);

	public Integer delete(Integer relationId);

}
